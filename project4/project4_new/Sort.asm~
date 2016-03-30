// The program inpuit will be at R14(starting address),R15(length of array).
// The program should sort the array starting at the address in R14 with 
// length specified in R15.
// The sort is in descending order - the largest number at the head of the array.
// The array is allocated in the heap address 2048-16383
	
	@14	// initialize indexes
	D = M
	@j
	M = D

	@15	// mark the end of the arr
	D = M
	@14
	D = M + D
	@endOfArr
	M = D

(START)

	@j	// set the max val to the first
	A = M
	D = M
	@maxVal
	M = D
	@j
	D = M
	@maxCell
	M = D

	@j
	D = M
	@i
	M = D + 1

(LOOP)		// find the max value in the array

	@i
	D = M
	@endOfArr
	D = D - M
	@SWEEP
	D;JEQ

	@i
	A = M
	D = M
	@maxVal 
	D = M - D
	@UPDATE_MAX
	D;JLT

	@i
	M = M + 1
	@LOOP
	0;JMP

(UPDATE_MAX)	// update the max value

	@i
	A = M
	D = M
	@maxVal
	M = D
	@i
	D = M
	@maxCell
	M = D

	@i
	M = M + 1
	@LOOP
	0;JMP

(OUTER_LOOP)

	@j
	D = M + 1
	@endOfArr
	D = M - D
	@END
	D;JEQ

	@j
	M = M + 1
	D = M
	@i
	M = D
	@START
	0;JMP
	
(SWEEP)

	@j
	A = M
	D = M
	@temp
	M = D

	@maxVal
	D = M
	@j
	A = M
	M = D

	@temp
	D = M
	@maxCell
	A = M
	M = D

	@OUTER_LOOP
	0;JMP

(END)
