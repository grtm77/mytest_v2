import shutil
import os
# 获取当前脚本所在目录的绝对路径
script_dir = os.path.dirname(os.path.abspath(__file__))
# 获取当前脚本所在目录的父目录的绝对路径
parent_dir = os.path.dirname(script_dir)

source_dir = os.path.join(script_dir, 'dist') # 源目录
destination_dir = os.path.join(parent_dir, 'src', 'main', 'resources', 'static')  # 目标目录
# 创建目标目录（如果不存在）
os.makedirs(destination_dir, exist_ok=True)

# 获取源目录中的文件列表
file_list = os.listdir(source_dir)

for file_name in file_list:
    # 拼接源文件的路径
    source_file = os.path.join(source_dir, file_name)
    # 检查路径是否为文件
    if os.path.isfile(source_file):
        # 拼接目标文件的路径
        destination_file = os.path.join(destination_dir, file_name)  # 目标文件的完整路径

        # 如果目标文件已存在，先删除它
        if os.path.exists(destination_file):
            os.remove(destination_file)

        # 复制文件到目标目录
        shutil.copy2(source_file, destination_file)


# 遍历源目录下的文件和子目录
for root, dirs, files in os.walk(source_dir):
    # # 复制文件
    # for file in files:
    #     source_file = os.path.join(root, file)  # 源文件的完整路径
    #     destination_file = os.path.join(destination_dir, file)  # 目标文件的完整路径
    #
    #     # 如果目标文件已存在，先删除它
    #     if os.path.exists(destination_file):
    #         os.remove(destination_file)
    #
    #     # 复制源文件到目标文件
    #     shutil.copy2(source_file, destination_file)

    # 创建子目录
    for dir in dirs:
        source_subdir = os.path.join(root, dir)  # 源子目录的完整路径
        destination_subdir = os.path.join(destination_dir, os.path.relpath(source_subdir, source_dir))  # 目标子目录的完整路径

        # 如果目标子目录已存在，先删除它
        if os.path.exists(destination_subdir):
            shutil.rmtree(destination_subdir)

        # 复制源子目录到目标子目录
        shutil.copytree(source_subdir, destination_subdir)

print("文件复制完成！")
