# Variables
SRC_DIR := src
BIN_DIR := bin
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
MAIN_CLASS := com.craftinginterpreters.lox.Lox

# Targets
.PHONY: all build clean run

# Allow positional args: make run arg1 arg2
RUN_ARGS := $(filter-out run,$(MAKECMDGOALS))

all: build

# Compile Java source files
build: $(SOURCES)
	@mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(SOURCES)

# Run the application
# Usage: make run ARGS="arg1 arg2" or make run arg1 arg2
run: build
	java -cp $(BIN_DIR) $(MAIN_CLASS) $(ARGS) $(RUN_ARGS)

# Ignore unknown targets so positional args don't error
%:
	@:

# Clean build artifacts
clean:
	rm -rf $(BIN_DIR)
