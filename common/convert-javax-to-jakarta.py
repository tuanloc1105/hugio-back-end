import os

# Define the directories where the generated Java files are located
generated_code_dir_list = [
    'tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/common',
    'tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/inventory',
    'tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/product',
    'tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/user',
    'tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/validation'
]

# Walk through the directories and replace javax.annotation.Generated with jakarta.annotation.Generated
for generated_code_dir in generated_code_dir_list:
    directory_to_proceed = os.path.join(os.getcwd(), generated_code_dir)
    print(f'Processing directory: {directory_to_proceed}')
    for subdir, _, files in os.walk(directory_to_proceed):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(subdir, file)
                print(f'Processing file: {file_path}')
                with open(file_path, 'r') as f:
                    content = f.read()
                # content = content.replace('javax.annotation.Generated', 'jakarta.annotation.Generated')
                content = content.replace('javax.', 'jakarta.')
                with open(file_path, 'w') as f:
                    f.write(content)

print("Replacement complete.")
