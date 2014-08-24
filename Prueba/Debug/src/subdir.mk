################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/BigFloat.cpp \
../src/BigInt.cpp \
../src/FloatType.cpp \
../src/Mandelbrot.cpp \
../src/main.cpp 

S_UPPER_SRCS += \
../src/bigmath128.S \
../src/bigmath64.S 

OBJS += \
./src/BigFloat.o \
./src/BigInt.o \
./src/FloatType.o \
./src/Mandelbrot.o \
./src/bigmath128.o \
./src/bigmath64.o \
./src/main.o 

CPP_DEPS += \
./src/BigFloat.d \
./src/BigInt.d \
./src/FloatType.d \
./src/Mandelbrot.d \
./src/main.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/%.o: ../src/%.S
	@echo 'Building file: $<'
	@echo 'Invoking: GCC Assembler'
	as  -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


