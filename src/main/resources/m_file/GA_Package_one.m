function [gy, x] = GA_Package_one(popsize, pc, pm, A, c, ii, Max_iter)
% 所有函数放置在一个文件
global B;
global D;
[pop, B, D, I] = initialization(A, popsize);    %初始化种群
for t = 1:Max_iter                                  %迭代次数
    [pop] = Duplicate(pop);                   % 去除重复个体
    [pop, All_W] = Repair(pop);               % 修补算子，修补不满足约束的解
    %% 计算本种群的适应度，根据适应度获取最佳个体
    fitvalue = Cal_fitness(pop, c, All_W);          %计算适应度
    [best_individual, ~] = best(pop, fitvalue);     %最优个体及其适应度值
    x(t,:) = best_individual;                       %最佳个体
    y(t) = best_individual * c';                    %计算最佳个体的目标函数值
    %% 通过选择、交叉、变异操作产生新种群
    [newpop1] = Select(pop, fitvalue);              %选择算子
    [newpop2] = Cross(newpop1, pc);                 %交叉算子
%     [newpop3] = Mutate(newpop2, pm);              %变异算子
    [newpop3] = Mutate(newpop2, pm, t, Max_iter);   %变异算子
    %% 最优保存策略
%     objvalue1 = Cal_obj_val(newpop3(1,:), c);       %准备用最优个体替换掉第一个个体
%     is_satis = satis_con(best_individual, D, sessor_num);   %检测是否满足约束
%     if objvalue1 > y(t) || ~is_satis                %目标函数值更大或者不满足约束条件则替换掉
%         newpop3(1,:) = best_individual;             %保留最佳个体
%     end
    % 用最优个体替换最差个体
    new_obj = Cal_obj_val(newpop3, c);              %计算所有个体目标函数值
    [~, the_ind] = max(new_obj);                    %找出最差个体
    newpop3(the_ind, :) = best_individual;          %用最优个体替代
    
    %% 保证每代结果递减
    if t>1 && y(t) > y(t-1)
        y(t)=y(t-1);                                %保证每代结果递减
    end
    pop=newpop3;                                    %产生新种群
end
%% 结果输出
Runtime_i = ii
[gy,opt_k] = min(y)              %y保存每次迭代的最优解，gy为全局最优解
y_max = max(y);

x = x(opt_k, :);                   %全局最优解对应的自变量
x = x(I);

% figure('Name','集合覆盖问题');grid on;hold on;
% plot(y,'-r')                     %最优收敛曲线
% title('GA算法--收敛性曲线');
% xlabel('进化代数');
% ylabel('函数值');
% % axis([0, Max_iter, 0, size(A, 2)]);
% axis([0,Max_iter,gy-1,y_max+1]);
end

%% 初始化种群
function [pop, B, D, I, Coverage_new] = initialization(Coverage, popsize)
    %% 初始化相关参数
    global sessor_num;
    global Ap_num;
    global K;
    %% 对覆盖矩阵进行列变换，按照覆盖节点数量降序进行排列
    % 对Ap进行排序操作
%     Cove_num = sum(Coverage);
%     [~, I] = sort(Cove_num, 'descend');
    % 不对Ap进行排序操作
    I = (1:Ap_num);
    Coverage_new = Coverage(:, I);
    
    B = cell(sessor_num, 1);         %元组元素 B{i} 表示覆盖节点 i 的Ap集
    D = cell(1, Ap_num);             %元组元素 D{j} 表示Ap点 j 覆盖的sessor集
    pop = zeros(popsize, Ap_num);
    All_W = zeros(sessor_num, popsize);
    %% 包含 sessor(i) 的Ap
    for i = 1: length(B)
        B{i} = find(Coverage_new(i,:) == 1);
        if length(B{i}) < K
            error('不满足K覆盖条件')
        end
    end
    
    %% Ap(j) 覆盖的 sessor
    for j = 1: length(D)
        D{j} = find(Coverage_new(:,j) == 1);     
    end
    
    %% 启发式产生一半的种群
    for k = 1 : popsize/2
        W = zeros(sessor_num, 1);       % 初始化覆盖情况向量
        X = [];                         % 个体包含的Ap集
        covered = [];                   % 特定Ap覆盖的sessor集
        new_X = [];                     % 0，1构成的个体
        %% 对每个sessor随机选择(K)个Ap覆盖
        for i = 1 : sessor_num       
            K_Ap = B{i}(randperm(length(B{i}), K));       %随机选择K个Ap
            %更新各个sessor的覆盖计数
            for j = 1:length(K_Ap)
                if ~ismember(K_Ap(j), X)
                    X = union(X, K_Ap(j));
                    covered_sessor = D{K_Ap(j)};
                    W(covered_sessor) = W(covered_sessor) + 1;
                end
            end
        end
        %% 删除冗余的Ap
        % X = sort(X, 'descend');         %贪心删除 优先删除覆盖sessor较少的Ap
        X = X(randperm(length(X)));       %随机删除 打乱顺序
        for j = 1:length(X)
            covered = D{ X(j) };          % X(j) 对应的Ap覆盖的sessor集
            if min(W(covered) > K) == 1
                W(covered) = W(covered) - 1;
                X(j) = 0;
            end
        end
        %% 将选中的Ap置为1
        new_X = X(logical(X));
        pop(k,new_X) = 1;
    end
    %% 随机产生剩下一半的种群
    k = popsize/2 + 1;
    pop(k:end, :) = round(rand(popsize/2, Ap_num));   
    pop = logical(pop);
end

%% 去重操作
function [new_pop] = Duplicate(pop)
global K;
global D;
new_pop = pop;

[U, ~, ib] = unique(pop,'rows');            % U中为唯一个体
tb = tabulate(ib); 
Index=find(tb(:,2)>1);                      % 重复个体所对应的下标
if size(Index, 1) == 0
    return
end

new_pop(1:size(U, 1), 1:size(U, 2)) = U;    % 新种群前一段为原种群中的元素
now_row_index = size(U, 1);                 % 新个体放入的位置

for i = 1:size(Index)
    cur_indual = U(tb(Index(i),1), :);      % 当前个体
    num = tb(Index(i),2);                   % 当前个体重复的数目
    
    %% 删除冗余的Ap
    cur_w = Cal_W(cur_indual);              % 当前个体的覆盖情况
    X = find(cur_indual);                   % 当前选取的Ap集
    X = X(randperm(length(X)));             % 随机删除，打乱顺序
%     X = sort(X, 'descend');

    % 遍历每个Ap，判断是否冗余
    for X_j = 1:length(X)
        covered = D{X(X_j)};                % X(j)对应的Ap所覆盖的sessor集
        if min(cur_w(covered) > K) == 1
            cur_w(covered) = cur_w(covered) - 1;
            cur_indual(X(X_j)) = 0;
        end
    end
    
    %% 由重复的个体产生新的个体，随机选择一定数量的列去除
    for dup_i = 1:num-1
%         max_Wu = sum(cur_indual);             % 个体中所含的Ap数
        new_indual = cur_indual;                % 初始化新个体
        if sum(cur_indual) < 3                  % 当个体所含的Ap数小于3个
            Wu = sum(logical(cur_indual));      % 则将列全部去除
        else
            Wu = randi(3);                      % 随机去除的列数介于1-3之间
        end
        exist_cols = find(cur_indual);          % 当前个体存在的列
        chosed_col_index = randperm(length(exist_cols), Wu);    % 选中的列索引
        chosed_col = exist_cols(chosed_col_index);              % 选中的列

        new_indual(chosed_col) = 0;                             % 选中的列置为0
        now_row_index = now_row_index + 1;
        new_pop(now_row_index, :) = new_indual;                 % 在种群中放入新个体
    end
end
end

%% 修补操作
function [pop, All_W] = Repair(pop)
% 修补算子
% pop:      种群
% A:        覆盖矩阵
% B:        B(i): 覆盖sessor_i 的Ap集
% D:        D(j): Ap_j 所覆盖的sessor集 
% All_W:    覆盖情况矩阵，列i 表示个体i的覆盖情况
    
    global K;
    global B;
    global D;
    %% 计算对应的W
    All_W = Cal_W(pop);
    
    %% 对不满足约束条件的进行处理
    satis = all(All_W >= K);            % 逻辑向量，判断每个个体是否满足覆盖条件
    dissatis_index = find(~satis);      % 不满足覆盖条件的个体索引
    for i1 = 1:length(dissatis_index)
        cur_indual = pop(dissatis_index(i1), :);    %当前不满足约束条件的个体
        chosed_Aps = find(cur_indual);               %个体已经选择的Ap
        cur_w = All_W( : , dissatis_index(i1));     %个体对应的覆盖情况
        unc_s_first = find(cur_w < K);                    %不满足覆盖条件的sessor集
        unc_s_true = unc_s_first;
        %% 使用Ap覆盖每个未达到覆盖条件的sessor
        for i = 1:length(unc_s_first)
            cur_s = unc_s_first(i);               %当前处理的sessor
            cur_w_i = cur_w(cur_s);         %当前sessor的覆盖值
            if cur_w_i < K                  %在循环的过程中会更新cur_w的值，因此需要进行判断
                candi_Ap = B{cur_s};                %覆盖当前sessor的Ap
                en_chosed = setdiff(candi_Ap, chosed_Aps);   %当前sessor可供选择的Ap
                need_Ap = K - cur_w_i;              %所缺少的覆盖个数
                %% 贪心选择 need_Ap 个进行覆盖
                for num_i = 1:need_Ap
                   Ap_score = zeros(size(en_chosed));
                   for en_chosed_i = 1:length(en_chosed)
                      cur_Ap = en_chosed(en_chosed_i);           %当前Ap
                      cur_cing = D{cur_Ap};           %当前覆盖
                      score = length(intersect(unc_s_true, cur_cing));    %衡量当前Ap好坏
                      Ap_score(en_chosed_i) = score; 
                   end
                   [~, index] = max(Ap_score);              %最好Ap的索引
                   cho_Ap = en_chosed(index);               %选择的Ap
                   chosed_Aps = union(chosed_Aps, cho_Ap);  %更新选择的Ap集
                   covered_sessor = D{cho_Ap};
                   cur_w(covered_sessor) = cur_w(covered_sessor) + 1;   %更新个体的覆盖情况
                   unc_s_true = find(cur_w < K);                    %更新不满足覆盖条件的sessor集
                   en_chosed = setdiff(en_chosed, cho_Ap);  %更新可供选择的Ap集
                   cur_indual(cho_Ap) = 1;                  %更新个体的0、1序列
                end

%                 %% 随机选择 need_Ap 个进行覆盖
%                 rand_chosed_index = randperm(length(en_chosed), need_Ap);
%                 rand_chosed = en_chosed(rand_chosed_index);
%                 cur_indual(rand_chosed) = 1;        %将对应的Ap置为1
%                 % 更新选取之后的覆盖情况
%                 for rand_chosed_i = 1:length(rand_chosed)
%                     the_D = D{rand_chosed(rand_chosed_i)};      %选中的Ap对应的覆盖sessor集
%                     cur_w(the_D) = cur_w(the_D) + 1;
%                 end
            end
        end
        %% 删除冗余的Ap
        X = find(cur_indual);             % 当前选取的Ap集
%         X = sort(X, 'descend');
        X = X(randperm(length(X)));       % 随机删除 打乱顺序
        for X_j = 1:length(X)
            covered = D{ X(X_j) };          % X(j) 对应的Ap覆盖的sessor集
            if min(cur_w(covered) > K) == 1
                cur_w(covered) = cur_w(covered) - 1;
                cur_indual(X(X_j)) = 0;
            end
        end
        All_W( : , dissatis_index(i1)) = cur_w;
        pop(dissatis_index(i1), :) = cur_indual;
    end
end


%% 计算适应度
function f_2 = Cal_fitness(pop, c, All_W)
% pop:    种群或者个体
% c:         权重系数
% All_W:           覆盖情况矩阵，列i表示个体i的覆盖情况
    f_up = size(pop,2) + 1;
    f = pop * c' ;
    % 不满足约束条件的适应值为0
%     disatis = find(~all(All_W));
    f_1 = f_up - f;
    disatis = ~all(All_W);
    f_1(disatis) = 0;
    
    % 开始计算f2, 对f1进行线性变换，此处本应使用f_1,但实验发现f更快收敛
    f_avg = mean(f);
    f_max = max(f);
    % 实验发现取f更快收敛
    %     f_avg = mean(f_1);
    %     f_max = max(f_1);
    if f_max == f_avg           %防止分母为0
        f_2 = f_1;
    else
        alpha = 0.5 * f_avg / (f_max - f_avg);
        beta = f_avg*(f_max - 1.5*f_avg)/(f_max - f_avg);
        f_2 = alpha*f_1 + beta;
    end
end


%% 最优个体及其适应度值
function [best_one,best_fit] = best(pop,fit_value)
% 返回最优个体
[px,~]=size(pop);
best_one=pop(1,:);
best_fit=fit_value(1);
for i=2:px;
    if fit_value(i) > best_fit
        best_fit = fit_value(i);
        best_one=pop(i,:);
    end
end
end


%% 选择算子
function [newpop1]=Select(pop, fit_value)
% fit_value:    个体适应度值
    %% 选择算子
    total_fit = sum(fit_value);     %适应度求和和
    ps = fit_value./total_fit;      %单个个体被选择的概率
    ps_sum = cumsum(ps);            %前几项累积和
    [px, ~] = size(pop);
    %% 根据产生的随机数确定入选的个体
    ms = sort(rand(px, 1));         %随机产生px个0,1之间的数，并按升序排列
    fit_in = 1;             %双指针法，通过滑动来确定入选个体
    new_indual = 1;         %当前正在选择的个体
    while new_indual <= px
        if (ms(new_indual) <= ps_sum(fit_in))
            newpop1(new_indual, :) = pop(fit_in, :);
            new_indual = new_indual + 1;
        else
            fit_in = fit_in+1;
        end
    end
end


%% 交叉算子
function [newpop2] = Cross(pop, pc)
% 交叉操作
% pc:     交叉概率
% %% 单点交叉
% [px,py]=size(pop);
% newpop2=ones(size(pop));
% for i=1:2:px-1
%     if rand<pc
%         cpoint=round(rand*py);  %随机产生一个交叉位
%         newpop2(i,:)=[pop(i,1:cpoint),pop(i+1,cpoint+1:py)];  %交换相邻两个个体交叉位之后的基因
%         newpop2(i+1,:)=[pop(i+1,1:cpoint),pop(i,cpoint+1:py)];
%     else
%         newpop2(i,:)=pop(i,:);
%         newpop2(i+1,:)=pop(i+1,:);
%     end
% end

% 启发式多点交叉
[px,py]=size(pop);
newpop2=zeros(size(pop));
Nu = ceil(py/4);          % 将基因串分成Nu+1个基因块
cross_points = randperm(py-2, Nu) + 1;   % 随机产生的一组交叉点
cross_points = sort(cross_points);             % 按升序排序
% 计算第一适应度
f_up = size(pop,2) + 1;
f = sum(pop, 2);
f_1 = f_up - f;

for i=1:2:px-1
    pc = 0.5 + (f_1(i) - f_1(i+1))/(f_1(i) + f_1(i+1));
    % 确定父代
    parent_1 = pop(i, :);
    parent_2 = pop(i+1, :);
    son_1 = zeros(1, py);
    son_2 = zeros(1, py);
    %% 比较基因片段
    %% 第一个片段
    p1_cut = parent_1(1:cross_points(1)-1);
    p2_cut = parent_2(1:cross_points(1)-1);
    % 如果相等则直接遗传给后代，否则基因片段按概率遗传给后代
    if isequal(p1_cut, p2_cut)
        son_1(1:cross_points(1)-1) = p1_cut;
        son_2(1:cross_points(1)-1) = p1_cut;
    else
        k = rand(1,2) < [pc, pc];
        if k(1)
            son_1(1:cross_points(1)-1) = p2_cut;
        else
            son_1(1:cross_points(1)-1) = p1_cut;
        end
        if k(2)
            son_2(1:cross_points(1)-1) = p2_cut;
        else
            son_2(1:cross_points(1)-1) = p1_cut;
        end
    end

    %% 第2个片段至倒数2个片段
    for j = 2:Nu
        p1_cut = [];
        p2_cut = [];
        p1_cut = parent_1(cross_points(j-1):cross_points(j)-1);
        p2_cut = parent_2(cross_points(j-1):cross_points(j)-1);
        if isequal(p1_cut, p2_cut)
            son_1(cross_points(j-1):cross_points(j)-1) = p1_cut;
            son_2(cross_points(j-1):cross_points(j)-1) = p1_cut;
        else
            k = rand(1,2) < [pc, pc];
            if k(1)
                son_1(cross_points(j-1):cross_points(j)-1) = p2_cut;
            else
                son_1(cross_points(j-1):cross_points(j)-1) = p1_cut;
            end
            if k(2)
                son_2(cross_points(j-1):cross_points(j)-1) = p2_cut;
            else
                son_2(cross_points(j-1):cross_points(j)-1) = p1_cut;
            end
        end
    end
    %% 最后一个片段
    p1_cut = [];
    p2_cut = [];
    p1_cut = parent_1(cross_points(Nu):end);
    p2_cut = parent_2(cross_points(Nu):end);
    if isequal(p1_cut, p2_cut)
        son_1(cross_points(Nu):end) = p1_cut;
        son_2(cross_points(Nu):end) = p1_cut;
    else
        k = rand(1,2) < [pc, pc];
        if k(1)
            son_1(cross_points(Nu):end) = p2_cut;
        else
            son_1(cross_points(Nu):end) = p1_cut;
        end
        if k(2)
            son_2(cross_points(Nu):end) = p2_cut;
        else
            son_2(cross_points(Nu):end) = p1_cut;
        end
    end
    newpop2(i,:) = son_1;
    newpop2(i+1,:) = son_2;
end
end


%% 变异算子
function [newpop3] = Mutate(pop, pm, t, max_t)
% function [newpop3] = Mutate( pop, pm)
% 变异操作
% pm:   变异概率

%% 对于每一位随机发生变异
% [px,py]=size(pop);
% k = rand(px,py);
% logc = k<pm;
% newpop3 = abs(logc - pop);


%% 自适应多位变异
[px,py]=size(pop);
newpop3 = zeros(px, py);
%%  计算第一适应度
f_up = size(pop,2) + 1;
f = sum(pop,2);
f_1 = f_up - f;
% 不满足约束条件的适应值为0
All_W = Cal_W(pop);
disatis = ~all(All_W);
f_1(disatis) = 0;

f_avg = mean(f_1);
f_max = max(f_1);
f_min = min(f_1);

%% 设置公式参数
% 变异位数逐渐增大，到后期取得最大并稳定下来
md = max_t*3/4;      % 迭代界限，超过此界限算法进入后期
mu = py/2;      % 算法后期稳定的变异位数, 
mg_1 = -(log(mu)/md);      % 算法早期使用，控制分母属于 1-mu+1 之间，使得早期至少有一位发生变异
mg_2 = -10;  %算法后期使用，使得分母趋近于1
me = 0.5;   % 使得分子在 0.5mu-1.5mu 之间

for i = 1:px
    %% 确定变异位数
    if f_1(i) > 0
        if t<md
            mutate_num = mu*(me + (f_max - f_1(i))/(f_max - f_min))/(1+exp(mg_1*(t-md)));
            mutate_num = round(mutate_num);
        else
            mutate_num = mu*(me + (f_max - f_1(i))/(f_max - f_min))/(1+exp(mg_2*(t-md)));
            mutate_num = round(mutate_num);
        end
    else
        if t<md
            mutate_num = round(mu/(1+exp(mg_1*(t-md))));
        else
            mutate_num = round(mu/(1+exp(mg_2*(t-md))));
        end
    end
    %% 开始变异
    index = randperm(py, mutate_num);
    indual = pop(i, :);
    indual(index) = ~indual(index);
    newpop3(i, :) = indual;
end
end

%% 计算函数值
function objvalue =Cal_obj_val(pop, c)
    objvalue = pop * c';
end

%% 判断是否满足约束条件
function bool = satis_con(indual, D, sessor_num)    
    W = zeros(sessor_num, 1);
    k_D = D(logical(indual));
    %% 累加覆盖值
    for j = 1 : length(k_D)
        covered_sessor = k_D{j};
        W(covered_sessor) = W(covered_sessor) + 1;
    end
    bool = all(W);
end

%% 计算种群的（也可以是单个个体）覆盖情况矩阵
function All_W = Cal_W(pop)
% 每一列对应每一个个体的覆盖情况
% All_W行数：为sessor个数
% All_W列数：为种群中个体数目

global sessor_num;
global D;
pop = logical(pop);

indual_num = size(pop, 1);       %个体数目
All_W = zeros(sessor_num, indual_num);          %初始化覆盖情况矩阵

%计算每个个体的覆盖情况
for j = 1:indual_num
    col_j = zeros(sessor_num, 1);       %初始化该个体覆盖情况为全0
    D_indual = {D{pop(j, :)}};    % 取出个体所有Ap的覆盖情况，用于累加
    for i = 1:size(D_indual, 2)        % 累加覆盖情况
        col_j(D_indual{i}) = col_j(D_indual{i}) + 1;
    end
    All_W(:, j) = col_j;
end
end