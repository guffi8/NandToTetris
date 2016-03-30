// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

@SCREEN
D=A-1
@i
M=D

// Coloring in the chosen color
(PAINT)
@COLOR
D=M
@i
A=M
M=D

(LOOP)
// Checking for keyboard input
@KBD
D=M
@BLACK
D;JNE
@WHITE
D;JEQ

(BLACK)
// Check if exceeding the screen
@i
D=M
@KBD
D=A-D
@LOOP
D;JEQ

//Set the direction when coloring in black
@i
M=M+1

// Paint in the color
@COLOR
M=-1
@PAINT
0;JMP

(WHITE)
// Set the color (also the color when exceeded the screen)
@COLOR
M=0

// Check if exceeding the screen
@i
D=M
@SCREEN
D=D-A
@LOOP
D;JLT

// Set the direction when coloring in white
@i
M=M-1

@PAINT
0;JMP
