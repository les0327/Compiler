.586
.model flat, c
.code
main proc
	LOCAL a:DWORD
	MOV a, 2
	LOCAL b:DWORD
	MOV b, offset a
	ret
main endp
