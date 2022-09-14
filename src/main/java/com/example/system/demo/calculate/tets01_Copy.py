import sys

import pulp

X=[]
Sub_set=[]
hashmap={}
def readfile01(filename):
	with open(filename,'r') as f:
		for line in f.readlines():
			linestr = line.strip()
			#print(linestr)
			linestrlist = linestr.split(" ")
			hashmap[linestrlist[0]]=linestrlist[1:len(linestrlist)]
			Sub_set.append(linestrlist[1:len(linestrlist)])
			#print (linestrlist)
			#linelist = map(int,linestrlist)# 方法一
			# linelist = [int(i) for i in linestrlist] # 方法二
			#print(linelist)

def readfile02(filename):
	with open(filename,'r') as f:
		for line in f.readlines():
			linestr = line.strip()
			X.append(linestr)
#命令行执行 这个脚本
# python D:\\IDE_workplace\\PY_workplace\\project01\\tets01.py  D:\\IDE_workplace\\PY_workplace\\project01\\linnerP_gateway.txt  D:\\IDE_workplace\\PY_workplace\\project01\linnerP_sensor.txt
# python D:\\IDE_workplace\\PY_workplace\\project01\\tets01_Copy.py  C:\Users\THTF\Desktop\gateWayData.txt  C:\Users\THTF\Desktop\sensorData.txt
readfile01(sys.argv[1])
readfile02(sys.argv[2])

len_X= len(X)
# 计算X中每个元素的频率并返回最大频率f-用于舍入法判断
all_f = [0] * len_X
for i in range(len(X)):
	for j in range(len(Sub_set)):
		if X[i] in Sub_set[j]:
			all_f[i] = all_f[i] + 1
f = max(all_f)

# 求解线性规划问题
# 目标函数：min CX  -----此问题中C取值为1
# 约束条件：AX>=B
# 定义C---目标函数的系数
C = [1] * len_X
A = [[0 for i in range(len_X)] for i in range(len_X)]
for i in range(len(X)):
	for j in range(len(Sub_set)):
		if X[i] in Sub_set[j]:
			A[i][j] = A[i][j] + 1
B = [1] * len_X

# 定义X
X = [pulp.LpVariable(f'x{i}', lowBound=0, upBound=1) for i in range(len_X)]

# 确定最大化最小化问题，最大化只要把Min改成Max即可
m = pulp.LpProblem(sense=pulp.LpMinimize)
m += pulp.lpDot(C, X)

# 设置约束条件
for i in range(len(A)):
	m += (pulp.lpDot(A[i], X) >= B[i])
m.solve()  # 求解

object_result = pulp.value(m.objective)
result = [pulp.value(var) for var in X]

# 舍入法保留最终结果
for item in range(len(X)):
	if result[item] >= 1 / f:
		result[item] = 1
	else:
		result[item] = 0

final_set = []
for item in range(len(Sub_set)):
	if result[item] == 1:
		final_set.append(Sub_set[item])
#print('可行解C：', final_set)
print('可行解C:length：', len(final_set));

Correctness=[]

#验证正确性
for i in range(len(final_set)):
	for j in range(len(final_set[i])):
		Correctness.append(final_set[i][j])
#去重new_li1 = list(set(Correctness))
#print(len(new_li1))
for k,v in hashmap.items():
	print(k)
print('12345')
for i in range(len(final_set)):
	list=[k for k, v in hashmap.items() if v == final_set[i]];
	if(len(list)!=1):
		print([list[0]]);
	else:
		print(list);

#测试hashmap长度
#print(len(hashmap))