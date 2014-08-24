.intel_syntax noprefix

.globl sum128

sum128:
    push rbp
    mov rbp,rsp

/*
dst   = rdi
src   = rsi
count = rdx
*/
      mov rcx,rdx;
      jrcxz addend;
      clc;
addloop:
      dec rcx;
      /*aca deberia levantar los datosy loopear con 128 bits*/

	  movdqu xmm0, [rsi+rcx*8]; 	//mov eax, [rsi+rcx*4];tengo los 128

  	  addsd xmm0, xmm0;

	  movdqu [rsi+rcx*8], xmm0;

      jrcxz addend;
      jmp addloop;
addend:

      pop rbp;
      ret

.globl mult128

mult128:
    push rbp
    mov rbp,rsp

    push rbx;
    push r8;
    push r9;

/*
dst   = rdi
src1  = rsi
src2  = rdx
count = rcx
*/

    mov r8, rcx;
    mov r9, rdx;

    xor rax,rax;
    inc eax;
    inc eax;
    mul ecx;
    mov ecx,eax;
    dec rcx; //count*2 -1;

    lea r9, [r9 +rcx*4]; //ultimo coso del segundo coso

    mov rbx,r8; //count;
    lea rsi,[rsi+rbx*4]; //primer coso

mulloop:
    test rbx,rbx;
    jnz mulloop2;
    mov rbx,r8;
    add rcx,rbx;
    dec rcx;

    sub r9, 4;
#    jrcxz mulend;

mulloop2:
    dec rbx;

    mov eax, dword ptr [rsi + rbx *4];

    mul dword ptr [r9];

    add dword ptr [ rdi + rcx * 4 ] , eax;

    jrcxz mulend;

    dec ecx;

    adc dword ptr [ rdi + rcx * 4 ], edx;

    jrcxz mulloop;

    dec ecx;

    adc dword ptr [ rdi + rcx * 4 ], dword ptr 0;

    inc ecx;

    jmp mulloop;

mulend:
    pop r9;
    pop r8;
    pop rbx;
    pop rbp;
    ret
