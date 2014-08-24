.intel_syntax noprefix

.globl sum
.globl _sum

_sum:
sum:
      push ebp
      mov ebp,esp;
      push edi;
      push esi;

      mov edi, [ebp+8];
      mov esi, [ebp+12];
      mov ecx,[ebp+16];

      xor edx,edx
      clc;
      jecxz addend;
addloop:
      dec ecx;
      mov eax, [esi+ecx*4];
      adc [edi+ecx*4], eax;

      jecxz addend;
      jmp addloop;
addend:
      jnc addnocarry
      inc edx;
addnocarry:

      mov eax, edx;

      pop esi;
      pop edi;
      pop ebp;
      ret

.globl _mult
.globl mult


_mult:
mult:
    push ebp
    mov ebp,esp

    push edi;
    push esi;
    push ebx;

    mov ebx, [ebp+20]; //count;

    mov eax, [ebp+12]; //src1
    lea eax, [eax + ebx *4];
    mov [ebp+12],eax;

    mov ecx, ebx;
    shl ecx;
    dec ecx; //count*2 -1;

    mov esi, [ebp+16];
    lea esi, [esi + ecx * 4]; //ultimo coso del segundo coso

    mov edi, [ebp+8]; //dst

mulloop:
    test ebx,ebx;

    jnz mulloop2;

    mov ebx, [ebp+20];
    add ecx,ebx;
    dec ecx;
    sub esi, 4;

mulloop2:
    dec ebx;

    mov eax, [ebp+12]; //src1
    mov eax, [eax + ebx *4];

    mul dword ptr [esi];

    add [ edi + ecx * 4 ] , eax;

    jecxz mulend;

    dec ecx;

    adc [ edi + ecx * 4 ], edx;

    jecxz mulloop;

    dec ecx;

    adc [ edi + ecx * 4 ], dword ptr 0;

    inc ecx;
    jmp mulloop;

mulend:
    pop ebx;
    pop esi;
    pop edi;
    pop ebp;
    ret

