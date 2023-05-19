function [x_return, g_return]  = GA_parse(Coverage)
    %% Set random number seeds
    % s = rng;
    % save('s.mat', 's');

    % load('s.mat');
    % rng(s);
    %% Set parameter
    global Ap_num;
    global sessor_num;
    global K;       % K Coverage

%     load('Coverage_4x4.mat');
    Max_iter= 500;      % iterations

    A = Coverage;
    Ap_num = size(Coverage, 2);
    sessor_num = size(Coverage, 1);
    K = 1;
    c = ones(1, Ap_num);

    %% -----------------------------------------------------
    popsize=100;        %种群规模
    pc=0.9;             %杂交概率
    pm=0.1;             %变异概率
    Runtime=1;          %重开始次数
    g = zeros(1, Runtime);        %用于保存每次Runtime最优值
    X = zeros(Runtime, Ap_num);   %用于保存每次Runtime最优个体
    %% -----------------------------------------------------
    for i = 1:Runtime
        [g(i), x] = GA_Package_one(popsize, pc, pm, A, c, i, Max_iter);
        X(i, :) = x;            % 保存每次运行最优个体
    end
    mean_g = mean(g);               % Calculate average value
    [min_g,min_k] = min(g);         % Calculate the best objective function value
    [max_g,max_k] = max(g);         % Calculate the worst objective function value
    std_g = std(g);                 % Calculate standard deviation
    x_return = X(min_k, :);
    g_return = min_g;
    %     min_g
    %     max_g
    %     mean_g
end