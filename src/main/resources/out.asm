.586
.model flat, c
.code
main proc
	LOCAL a:DWORD
	MOV a, 3
	LOCAL b:DWORD
	MOV b, offset a
	LOCAL f:DWORD
	MOV f, offset a
	LOCAL c:DWORD
	MOV eax, [b]
	MOV ebx, 12
	ADD eax, ebx
	push eax
	pop c
	MOV eax, [f]
	MOV ebx, [b]
	CMP eax, ebx
	jl @C0
	push 0
	jmp @endC0
@C0:
	push 1
@endC0:
	pop eax
	CMP eax, 0
	je @if0
	MOV eax, c
	MOV ebx, [b]
	ADD eax, ebx
	push eax
	pop a
	jmp @endIf0
@if0:
	MOV c, [b]
@endIf0:
ret
main endp
