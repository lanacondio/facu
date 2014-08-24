################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/BigFloat.cpp \
../src/BigInt.cpp \
../src/Mandelbrot.cpp \
../src/main.cpp 

S_UPPER_SRCS += \
../src/bigmath32.S \
../src/bigmath64.S 

OBJS += \
./src/BigFloat.o \
./src/BigInt.o \
./src/Mandelbrot.o \
./src/bigmath32.o \
./src/bigmath64.o \
./src/main.o 

CPP_DEPS += \
./src/BigFloat.d \
./src/BigInt.d \
./src/Mandelbrot.d \
./src/main.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/%.o: ../src/%.S
	@echo 'Building file: $<'
	@echo 'Invoking: GCC Assembler'
	as  -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


