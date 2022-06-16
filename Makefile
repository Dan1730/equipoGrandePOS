OUT_DIR = build
DOC_DIR = docs

JFLAGS_DELIMETER = :
PATH_SEP = /
ifeq ($(OS),Windows_NT)
	JFLAGS_DELIMETER = ;
	RM = del
	PATH_SEP = \\
endif

JCFLAGS = -g -d ${OUT_DIR}
JC = javac
JFLAGS = -cp ".${PATH_SEP}${OUT_DIR}${JFLAGS_DELIMETER}.${PATH_SEP}postgresql-42.2.8.jar"
JVM = java
JDOC = javadoc
DFLAGS = -d ${DOC_DIR}

ENTRY_POINT = StartPage

BACKEND_DIR = BackEnd
FRONTEND_DIR = FrontEnd

BACKEND_FILES = $(wildcard ${BACKEND_DIR}${PATH_SEP}*.java)
FRONTEND_FILES = $(wildcard ${FRONTEND_DIR}${PATH_SEP}*.java)
TOPLEVEL_FILES = $(wildcard *.java)

ALL_FILES = ${BACKEND_FILES} ${FRONTEND_FILES}  ${TOPLEVEL_FILES}

RUN: CLEAN ALL
	${JVM} ${JFLAGS} ${ENTRY_POINT}

ALL: CLEAN
	${JC} ${JCFLAGS} ${ALL_FILES}

CLEAN:
	${RM} ${OUT_DIR}${PATH_SEP}*.class

DOCS: 
	${JDOC} ${DFLAGS} ${ALL_FILES}