import os

def list_files(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                print(f'Processing file: {file_path}')
                with open(file_path, 'r') as f:
                    content = f.read()
                content = content.replace('javax.', 'jakarta.')
                
                content = content.replace('GeneratedMessage;', 'GeneratedMessageV3;')
                content = content.replace('GeneratedMessage ', 'GeneratedMessageV3 ')
                content = content.replace('GeneratedMessage)', 'GeneratedMessageV3)')
                
                # content = content.replace('GeneratedMessageV3;', 'GeneratedMessage;')
                # content = content.replace('GeneratedMessageV3 ', 'GeneratedMessage ')
                # content = content.replace('GeneratedMessageV3)', 'GeneratedMessage)')
                
                with open(file_path, 'w') as f:
                    f.write(content)
# Specify the directory you want to search
directory = os.path.join(os.getcwd(), 'tdtu-proto-lib')

list_files(directory)
