import os
from os import listdir
from os.path import isfile, join
from subprocess import check_call

path = "/home/yuval/university/Nand/MyProjets/project11/src/"
testPath = "/home/yuval/university/Nand/MyProjets/project11/src/test/testFor10-11/"

javaFiles = [f for f in listdir(path) if isfile(path + f) and f.endswith(".java")]

print("start compilation")

for javaFile in javaFiles:
	res = os.system("javac " + javaFile)
	if (res != 0):
		print("compilation faild!")
		exit()

print("compilation finished")

#folders = ["ArrayTest", "Square", "mustPassFiles", "hardFiles", "ExpressionlessSquare"]
folders = ["mustPassFiles", "hardFiles"]

for folder in folders:
	print("generate .vm to the folder : " + folder)
	os.system("java JackCompiler test/testFor10-11/" + folder + "/")

status = []

for folder in folders:

	print("")
	print("Test the folder : " + folder)
	print("")

	pathMy = testPath + folder + "/"
	pathToTest = testPath + folder + "/" + folder + "/"

	os.chdir("/")

	vmFiles = [f for f in listdir(pathMy) if isfile(pathMy + f) and f.endswith(".vm")]

	vmFiles.sort()

	for i in range(0, len(vmFiles)):
		print("compare : " + vmFiles[i])
		res = os.system("sh ~/university/Nand/tools/TextComparer.sh " + pathMy + vmFiles[i] + " " + pathToTest + vmFiles[i])
		if (res != 0):
			status.append([folder, vmFiles[i]])

print("\n-------------------------------------------------------------------------")

if (len(status) == 0):
	print("All Tests Passed!!")
else:
	print("Some tests are faild : ")
	for fail in status:
		print("\tfolder : " + fail[0] + ", file : " + fail[1])

print("-------------------------------------------------------------------------")
