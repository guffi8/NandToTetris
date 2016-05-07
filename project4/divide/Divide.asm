// The program input will be at R13,R14 while the result R13/R14 
// will be store at R15.
// The remainder should be discarded.
// You may assume both numbers are positive.
// The program should have a running time logarithmic with 
// respect to the input

	@14	
	D = M
	@END
	D;JEQ	// check if dividing by zero
	@15
	M = 0
	@13
	D = M
	@END
	D;JEQ	// check if R13 == 0

	@13
	D = M
	@dividend
	M = D
	@14
	D = M
	@divisor
	M = D
	@15
	M = 0
	@dividendShiftCounter
	M = 0
	@divisorShiftCounter
	M = 0
	
(DIVIDEND_SHIFT_COUNT)	// count number of shifting in R13
	@dividend
	D = M
	@DIVISOR_SHIFT_COUNT
	D;JEQ
	
	@dividend
	M = M>>
	@dividendShiftCounter
	M = M + 1
	@DIVIDEND_SHIFT_COUNT
	0;JMP
	
(DIVISOR_SHIFT_COUNT)	// count number of shifting in R14
	@divisor
	D = M
	@SHIFT_DIFFERENCE
	D;JEQ
	
	@divisor
	M = M>>
	@divisorShiftCounter
	M = M + 1
	@DIVISOR_SHIFT_COUNT
	0;JMP
	
(SHIFT_DIFFERENCE)	// calculate the differnce in the shifting of R13, R14
	@dividendShiftCounter
	D = M
	@divisorShiftCounter
	D = D - M
	@shiftDifferenceVal
	M = D
	D = M
	@dividingIterations
	M = D + 1 

	@13
	D = M
	@dividend
	M = D
	@14
	D = M
	@divisor
	M = D
	
(DIVISOR_SHIFTING)	// Align the most-significant bit for the DIVISOR & DIVIDEND
	
	@shiftDifferenceVal
	D = M
	@DIVIDING
	D;JEQ

	@shiftDifferenceVal
	M = M - 1
	@divisor
	M = M<<
	@DIVISOR_SHIFTING
	0;JMP

(DIVIDING)		// the divide opeartion: calaculate the differnce between the DIVIDEND & DIVISOR.
			// if the differnce is positive add 1 to the answer and shift right the divisor, 
			// and update the dividend to be the differnce.
			// otherwise just shift right the divisor. on each iteration shift left the answer.


	@dividingIterations
	D = M
	@END
	D;JEQ
	
	@15
	M = M<<

	@dividingIterations
	M = M - 1

	@dividend
	D = M
	@divisor
	D = D - M
	@difference
	M = D
	D = M

	@POSITIVE
	D;JGE
	@NEGATIVE
	0;JMP


(POSITIVE)		// if the differnce between the dividend & divisor is possitive add 1 to the answer, 
			// and update the divident to be the differnce
	
	@difference
	D = M
	@dividend
	M = D
	@15
	M = M + 1
	@divisor
	M = M>>
	@DIVIDING
	0;JMP

(NEGATIVE)		// if the differnce between the dividend & divisor is negative 

	@divisor
	M = M>>
	@DIVIDING
	0;JMP
	
	
(END)
