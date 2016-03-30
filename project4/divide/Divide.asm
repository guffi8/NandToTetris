// The program input will be at R13,R14 while the result R13/R14 
// will be store at R15.
// The remainder should be discarded.
// You may assume both numbers are positive.
// The program should have a running time logarithmic with 
// respect to the input

	@COUNTER
	M = 0
	@15
	M = 0

	// Check if divided by 0
	@14
	D = M
	@END
	D;JEQ

(LOOP)
	// Add the number in R14 to the counter
	@14
	D = M
	@COUNTER
	M = D + M

	// Check if the counter pass the number in R13
	@13
	D = M
	@COUNTER
	D = D - M
	@END
	D;JLT

	// Add 1 to the answer in R15
	@15
	M = M + 1

	@LOOP
	0;JMP

(END)
