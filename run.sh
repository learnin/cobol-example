#!/bin/bash
set -e

SRC=src
OUT=out

# コンパイル
mkdir -p "$OUT"
javac -d "$OUT" \
  "$SRC/cobol/sample1/CobolUtil.java" \
  "$SRC/cobol/sample1/ExternalArea.java" \
  "$SRC/cobol/sample1/HogeDate.java" \
  "$SRC/cobol/sample1/WorkArea1.java" \
  "$SRC/cobol/sample1/WorkArea2.java" \
  "$SRC/cobol/sample1/CopyWorkArea1.java" \
  "$SRC/cobol/sample1/CopyWorkArea2.java" \
  "$SRC/cobol/sample1/Sub1.java" \
  "$SRC/cobol/sample1/Sample1.java"

# 実行
java -cp "$OUT" cobol.sample1.Sample1
