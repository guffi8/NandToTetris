// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[3], respectively.)

	@2
    	M = 0 // initialize sum
(LOOP)
	@1
	D = M
	@END
	D;JEQ // check if R1 < 0
	@0
	D = M
	@2
	M = M + D // add R0 to R2
	@1
	M = M - 1 // reduce 1 from R1 
	@LOOP
	0;JMP
(END)
	
