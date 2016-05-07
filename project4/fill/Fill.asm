// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

	@SCREEN
	D = A
	@pix
	M = D

(LOOP) // The main loop, check if any key is pressed
	@KBD
	D = M
	@BLACKEN
	D;JNE
	@CLEANED
	D;JEQ
	@LOOP
	0;JMP

(BLACKEN) // Draw on the screen

	// Check if we still on the screen
	@pix
	D = M
	@KBD
	D = D - A
	@LOOP
	D;JEQ

	// Draw
	@pix
	D = M
	M = M + 1
	A = D
	M = -1
	@LOOP
	0;JMP // Jump to the main loop

(CLEANED) // Clean the screen

	//  Check if we still on the screen
	@pix
	D = M
	@SCREEN
	D = D - A
	@LOOP
	D;JLT

	// Clean the screen
	@pix
	D = M
	M = M - 1
	A = D
	M = 0
	@LOOP
	0;JMP // Jump to the main loop
