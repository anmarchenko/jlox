# Variables
SRC_DIR := src
BIN_DIR := bin
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
MAIN_CLASS := com.craftinginterpreters.lox.Lox
GEN_AST_CLASS := com.craftinginterpreters.tool.GenerateAst
AST_DIR := $(SRC_DIR)/com/craftinginterpreters/lox

# Targets
.PHONY: all build clean run generate-ast

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

# Generate AST classes
generate-ast: build
	java -cp $(BIN_DIR) $(GEN_AST_CLASS) $(AST_DIR)

# Ignore unknown targets so positional args don't error
%:
	@:

# Clean build artifacts
clean:
	rm -rf $(BIN_DIR)
