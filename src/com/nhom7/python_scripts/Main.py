from Program import Program
import sys

def main():
    length, width, file_path, mode = load_data()
    mode = int(mode)
    program = Program(length, width)
    program.read_file(file_path)
    program.init_data()
    program.draw(length, width, mode)

def load_data():
    arguments = sys.argv
    file_path = arguments[1]
    mode = arguments[2]

    file = open(file_path, 'r')
    length = width = 0
    while True:
        line = file.readline()
        line = line.rstrip("\n")
        result = line.split(",")
        length = int(result[0])
        width = int(result[1])
        break
    file.close()
    return [length, width, file_path, mode]

if __name__ == "__main__":

    main()
    
