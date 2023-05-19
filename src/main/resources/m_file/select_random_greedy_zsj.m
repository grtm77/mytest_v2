function [Set_AP,ap_count]=select_random_greedy(Coverage) 
% 朴素贪心算法 
L_ap = size(Coverage, 2); 
L_sensors = size(Coverage, 1); 

Set_Sensor=zeros(1,L_sensors); %Sensor集合 
Set_AP=zeros(1,L_ap); %AP集合 
ap_count=0; 
sensor_count=L_sensors; 
while(sensor_count>0) 
    next_ap=select_ap(Coverage,Set_AP,L_ap,Set_Sensor,L_sensors); 
    ap_count=ap_count+1; 
    if((next_ap>0) && (next_ap<=L_ap)) 
        Set_AP(next_ap)=1; 
        for j=1:L_sensors %将next_ap能覆盖的节点标明为1 
            if((Coverage(j,next_ap)==1) &&(Set_Sensor(j)==0)) 
                Set_Sensor(j)=1; 
                sensor_count=sensor_count-1; 
            end
        end 
    end 
end 
end


%获取下一个AP节点 
function next_ap=select_ap(Coverage,Set_AP,L_ap,Set_Sensor,L_sensors) 
max_cc=0; 
next_ap=0; 
for i=1:L_ap 
cc=0; 
if((Set_AP(i)==0)) %AP_i未被选中，且AP_i覆盖节点next_sensor 
for j=1:L_sensors %统计AP_i所能覆盖的sensor的数量 
if((Coverage(j,i)==1) &&(Set_Sensor(j)==0)) 
cc=cc+1; 
end 
end 
if(cc>max_cc) 
max_cc=cc; 
next_ap=i; 
end 
end 
end 
end