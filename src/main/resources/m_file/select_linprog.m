function [x,fval]=select_linprog(Coverage)
L_ap = size(Coverage, 2);
L_sensors = size(Coverage, 1);

f=ones(1,L_ap);
A=(-1)*Coverage;
b=-1*ones(1,L_sensors);
intcon=[1:1:L_ap];
lb=zeros(L_ap,1);
ub=ones(L_ap,1);

[x,fval]=intlinprog(f,intcon,A,b,[],[],lb,ub);
end