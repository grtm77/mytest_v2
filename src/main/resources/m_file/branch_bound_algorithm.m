%% 利用matlab求解完全整数规划问题的分支定界法
%主函数
function [bb_result,bb_num]=branch_bound_algorithm(Coverage)
    global result; % 存储所有整数解
    global lowearBound; % 下界
    global upperBound; % 上界
    global count; % 用以判断是否为第一次分支
    
    L_ap = size(Coverage, 2);
    L_sensors = size(Coverage, 1);
    
    count = 1;

    f=ones(1,L_ap);
    A=(-1)*Coverage;
    b=-1*ones(1,L_sensors); %如果是2覆盖，则是乘以-2
    lbnd=zeros(L_ap,1);
    ubnd=ones(L_ap,1);
    Aeq = [];
    beq = [];

    %{
    f=[-40,-90];
    A=[8,7;7,20;];
    b=[56;70];
    Aeq = [];
    beq = [];
    lbnd = [0;0];		% 生成 0 矩阵
    ubnd = [inf;inf];		% 生成 1 矩阵
    %}

    BinTree = createBinTreeNode({f, A, b, Aeq, beq, lbnd, ubnd});
    if ~isempty(result)
        [fval,flag]=min(result(:, end)); % result中每一行对应一个整数解及对应的函数值
        Result=result(flag, :);
        disp('该整数规划问题的最优解为：');
        disp(Result(1,1:end-1));
        disp('该整数规划问题的最优值为：');
        disp(Result(1,end));
        bb_result= Result(1,1:end-1);
        bb_num= Result(1,end);
    else
        disp('该整数规划问题无可行解');
        bb_result=0;
        bb_num=0;
    end
    clear global;
end


%% createBinTreeNode.m
% 构建二叉树，每一结点对应一解
function BinTree = createBinTreeNode(binTreeNode)
    
    global result;
    global lowerBound;
    global upperBound;
    global count;

    if isempty(binTreeNode)
        return;
    else
        BinTree{1} = binTreeNode;
        BinTree{2} = [];
        BinTree{3} = [];
        
        options = optimoptions('linprog','Algorithm','dual-simplex');
        [x, fval, exitflag] = linprog(binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
            binTreeNode{4}, binTreeNode{5}, binTreeNode{6}, binTreeNode{7}, options);
        if count == 1
            %         upperBound = 0; % 初始下界为空
            lowerBound = fval;
            count = 2;
        end

        if ~IsInRange(fval)
            return;
        end

        if exitflag == 1
            flag = [];
            % 寻找非整数解分量
            for i = 1 : length(x)
                if abs(round(x(i))-x(i)) >1e-05
                    flag = i;
                    break;
                end
            end
            % 分支
            if ~isempty(flag)
                lowerBound = max([lowerBound; fval]);
                %OutputLowerAndUpperBounds();
                lbnd = binTreeNode{6};
                ubnd = binTreeNode{7};
                lbnd(flag, 1) = ceil(x(flag, 1)); % 朝正无穷四舍五入
                ubnd(flag, 1) = floor(x(flag, 1));

                % 进行比较，优先选择目标函数较大的进行分支
                [~, fval1] = linprog(binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                    binTreeNode{4}, binTreeNode{5}, binTreeNode{6}, ubnd, options);
                [~, fval2] = linprog(binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                    binTreeNode{4}, binTreeNode{5}, lbnd, binTreeNode{7}, options);
                if fval1 < fval2
                    % 创建左子树
                    BinTree{2} = createBinTreeNode({binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                        binTreeNode{4}, binTreeNode{5}, binTreeNode{6}, ubnd});
                    count = count + 1;

                    % 创建右子树
                    BinTree{3} = createBinTreeNode({binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                        binTreeNode{4}, binTreeNode{5}, lbnd, binTreeNode{7}});
                    count = count + 1;
                else
                    % 创建右子树
                    BinTree{3} = createBinTreeNode({binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                        binTreeNode{4}, binTreeNode{5}, lbnd, binTreeNode{7}});
                    count = count + 1;
                    % 创建左子树
                    BinTree{2} = createBinTreeNode({binTreeNode{1}, binTreeNode{2}, binTreeNode{3}, ...
                        binTreeNode{4}, binTreeNode{5}, binTreeNode{6}, ubnd});
                    count = count + 1;
                end
            else
                upperBound = min([upperBound; fval]);
                % OutputLowerAndUpperBounds();
                result = [result; [x', fval]];
                return;
            end
        else
            % 剪枝
            return;
        end
    end
end

%% IsInRange.m
% 判断分支问题的解是否在上下界的范围中，若不在，剪去
function y = IsInRange(fval)
    global lowerBound;
    global upperBound;

    if isempty(upperBound) && fval >= lowerBound
        y = 1;
    elseif  (fval >= lowerBound && fval <= upperBound)
        y = 1;
    else
        y = 0;
    end
end
