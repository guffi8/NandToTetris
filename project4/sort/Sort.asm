// The program input will be at R14(starting address),R15(length of array).
// The program should sort the array starting at the address in R14 with 
// length specified in R15.
// The sort is in descending order - the largest number at the head of the array.
// The array is allocated in the heap address 2048-16383

	@15
	D = M
	@i
	M = D
	
(OUTER_LOOP)
	@i
	M = M - 1
	@j
	M = -1
	// Stop condition
	@i
	D = M
	@END
	D;JEQ

(INNER_LOOP)
	@j
	M = M + 1
	D = M
	@i
	D = D - M
	@OUTER_LOOP
	D;JEQ
	
	@14
	D = M
	@j
	D = D + M
	A = D
	D = M
	@temp1
	M = D

	@14
	D = M
	@j
	D = D + M + 1
	A = D
	D = M
	@temp2
	M = D

	@temp1
	D = M
	@temp2
	D = D - M
	
	@SWEEP
	D;JLT
	@INNER_LOOP
	0;JMP


(SWEEP) // to complete the sweep
	@14
	D = M
	@j
	D = D + M
	A = D
	

(END)
		
