package cobol.sample1;

/**
 * COBOL メインプログラム SAMPLE1 に相当するクラス。
 *
 * 各 COBOL SECTION を private メソッドとして実装し、
 * PERFORM 文の呼び出し順序を run() メソッドで再現する。
 *
 * COBOL 元定義（sample1.cbl）:
 *   PROGRAM-ID. SAMPLE1.
 */
public class Sample1 {

    // --- WORKING-STORAGE SECTION ---

    // 01 WORK-AREA1（REDEFINES を含む）
    private final WorkArea1 workArea1 = new WorkArea1();

    // 01 WORK-AREA2
    private final WorkArea2 workArea2 = new WorkArea2();

    // 01 EXT-WORK-AREA IS EXTERNAL — SUB1 と同一インスタンスを共有
    private static final ExternalArea extWorkArea = ExternalArea.INSTANCE;

    // COPY "COPY1" REPLACING LEADING ==FOO== BY ==BAR== の展開結果
    // 01 COPY-WORK-AREA1
    private final CopyWorkArea1 copyWorkArea1 = new CopyWorkArea1();
    // 01 COPY-WORK-AREA2（FOO-STR* → BAR-STR* 置換済み）
    private final CopyWorkArea2 copyWorkArea2 = new CopyWorkArea2();

    // --- FILE SECTION ---
    // SELECT OUT-FILE ASSIGN TO "OUTFILE.TXT" ORGANIZATION IS LINE SEQUENTIAL.
    // FD OUT-FILE.
    // 01 OUT-FILE-REC PIC X(99).
    private String outFileRec = CobolUtil.move("", 99);

    // --- PROCEDURE DIVISION ---

    /**
     * PROCEDURE DIVISION のメインフロー。
     * STOP RUN はこのメソッドの終了で表現する。
     */
    public void run() {
        // PERFORM MOVE-GRP-SEC
        moveGrpSec();
        // PERFORM REDEFINES-SEC
        redefinesSec();
        // PERFORM MOVE-CORR-SEC
        moveCorrSec();
        // PERFORM CALL-SEC
        callSec();
        // PERFORM COPY-SEC
        copySec();
        // PERFORM OUT-FILE-SEC
        outFileSec();

        // 多階層のデータ項目へのアクセス
        // MOVE "01" TO DD OF HOGE-DATE OF WORK-AREA2
        workArea2.hogeDate.setDd("01");
        // DISPLAY "DD: " DD OF HOGE-DATE OF WORK-AREA2
        CobolUtil.display("DD: ", workArea2.hogeDate.getDd());

        // STOP RUN
    }

    /**
     * 集団項目間の転記
     *
     * COBOL:
     *   MOVE-GRP-SEC SECTION.
     *     MOVE "20260102" TO YYYYMMDD OF WORK-AREA1.
     *     MOVE WORK-AREA1 TO WORK-AREA2.
     *     DISPLAY "WORK-AREA2: " WORK-AREA2.
     */
    private void moveGrpSec() {
        // MOVE "20260102" TO YYYYMMDD OF WORK-AREA1
        workArea1.setYYYYMMDD("20260102");
        // MOVE WORK-AREA1 TO WORK-AREA2 — グループ間転記（文字列経由）
        workArea2.fromCobolString(workArea1.toCobolString());
        // DISPLAY "WORK-AREA2: " WORK-AREA2
        CobolUtil.display("WORK-AREA2: ", workArea2.toCobolString());
    }

    /**
     * REDEFINES された項目への MOVE
     *
     * COBOL:
     *   REDEFINES-SEC SECTION.
     *     MOVE "03" TO MM OF WORK-AREA1.
     *     DISPLAY "YYYYMMDD: " YYYYMMDD.
     */
    private void redefinesSec() {
        // MOVE "03" TO MM OF WORK-AREA1（YYYYMMDD-R ビュー経由で buf[] を部分書き換え）
        workArea1.setMM("03");
        // DISPLAY "YYYYMMDD: " YYYYMMDD（同じ buf[] をフラットビューで読む → "20260302"）
        CobolUtil.display("YYYYMMDD: ", workArea1.getYYYYMMDD());
    }

    /**
     * MOVE CORRESPONDING
     *
     * COBOL:
     *   MOVE-CORR-SEC SECTION.
     *     MOVE CORR YYYYMMDD-R TO HOGE-DATE.
     *     DISPLAY "HOGE-DATE: " HOGE-DATE.
     *
     * YYYYMMDD-R と HOGE-DATE は YYYY / MM / DD の 3 フィールドが同名のため
     * すべてコピー対象。静的に列挙して代入する。
     * DD は HOGE-DATE 側が PIC 9(2) COMP-3 のため setDd() で数値変換される。
     */
    private void moveCorrSec() {
        // MOVE CORR YYYYMMDD-R TO HOGE-DATE
        workArea2.hogeDate.setYyyy(workArea1.getYYYY()); // YYYY → YYYY
        workArea2.hogeDate.setMm(workArea1.getMM());     // MM   → MM
        workArea2.hogeDate.setDd(workArea1.getDD());     // DD (PIC X(2)) → DD (PIC 9(2) COMP-3)
        // DISPLAY "HOGE-DATE: " HOGE-DATE
        CobolUtil.display("HOGE-DATE: ", workArea2.hogeDate.toCobolString());
    }

    /**
     * CALL（参照渡し / 値渡し）
     *
     * COBOL:
     *   CALL-SEC SECTION.
     *     MOVE "EXTERNAL" TO EXT-WORK-DATA.
     *     CALL "SUB1" USING HOGE-DATE.          ← 参照渡し
     *     MOVE "20260102" TO HOGE-DATE.
     *     CALL "SUB1" USING CONTENT HOGE-DATE.  ← 値渡し
     */
    private void callSec() {
        // MOVE "EXTERNAL" TO EXT-WORK-DATA
        extWorkArea.setExtWorkData("EXTERNAL");
        // DISPLAY "EXT-WORK-DATA: " EXT-WORK-DATA
        CobolUtil.display("EXT-WORK-DATA: ", extWorkArea.getExtWorkData());

        // CALL "SUB1" USING HOGE-DATE — 参照渡し: 同一オブジェクトを渡す
        Sub1.call(workArea2.hogeDate);
        // DISPLAY "HOGE-DATE: " HOGE-DATE（SUB1 が YYYY を "2027" に変更済み）
        CobolUtil.display("HOGE-DATE: ", workArea2.hogeDate.toCobolString());
        // DISPLAY "EXT-WORK-DATA: " EXT-WORK-DATA（SUB1 が "SUB1" に変更済み）
        CobolUtil.display("EXT-WORK-DATA: ", extWorkArea.getExtWorkData());

        // MOVE "20260102" TO HOGE-DATE
        workArea2.hogeDate.fromCobolString("20260102");
        // DISPLAY "HOGE-DATE: " HOGE-DATE
        CobolUtil.display("HOGE-DATE: ", workArea2.hogeDate.toCobolString());

        // CALL "SUB1" USING CONTENT HOGE-DATE — 値渡し: コピーを生成してから渡す
        HogeDate copy = new HogeDate();
        copy.fromCobolString(workArea2.hogeDate.toCobolString());
        Sub1.call(copy);

        // DISPLAY "HOGE-DATE: " HOGE-DATE
        // 値渡しのため呼び出し側の hogeDate は変更されていない。
        CobolUtil.display("HOGE-DATE: ", workArea2.hogeDate.toCobolString());
    }

    /**
     * COPY
     *
     * COBOL:
     *   COPY-SEC SECTION.
     *     MOVE "COPY-DATA" TO COPY-WORK-DATA.
     *     MOVE "FOO1" TO BAR-STR1.
     *     ...
     */
    private void copySec() {
        // MOVE "COPY-DATA" TO COPY-WORK-DATA
        copyWorkArea1.setCopyWorkData("COPY-DATA");
        // DISPLAY "COPY-WORK-DATA: " COPY-WORK-DATA
        CobolUtil.display("COPY-WORK-DATA: ", copyWorkArea1.getCopyWorkData());

        // MOVE "FOO1" TO BAR-STR1 / BAR-STR2 / BAR-STR3
        copyWorkArea2.setBarStr1("FOO1");
        copyWorkArea2.setBarStr2("FOO2");
        copyWorkArea2.setBarStr3("FOO3");
        // DISPLAY
        CobolUtil.display("BAR-STR1: ", copyWorkArea2.getBarStr1());
        CobolUtil.display("BAR-STR2: ", copyWorkArea2.getBarStr2());
        CobolUtil.display("BAR-STR3: ", copyWorkArea2.getBarStr3());
    }

    /**
     * ファイル出力
     *
     * COBOL:
     *   OUT-FILE-SEC SECTION.
     *     DISPLAY "OUT-FILE: " WORK-AREA2.
     *     OPEN OUTPUT OUT-FILE.
     *     MOVE WORK-AREA2 TO OUT-FILE-REC.
     *     WRITE OUT-FILE-REC.
     *     CLOSE OUT-FILE.
     *
     * ORGANIZATION IS LINE SEQUENTIAL の WRITE は1レコード = 1行として出力する。
     */
    private void outFileSec() {
        // DISPLAY "OUT-FILE: " WORK-AREA2
        CobolUtil.display("OUT-FILE: ", workArea2.toCobolString());

        // MOVE WORK-AREA2 TO OUT-FILE-REC
        outFileRec = CobolUtil.move(workArea2.toCobolString(), 99);

        // OPEN OUTPUT OUT-FILE / WRITE OUT-FILE-REC / CLOSE OUT-FILE
        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.FileWriter("OUTFILE.TXT"))) {
            pw.println(outFileRec);
        } catch (java.io.IOException e) {
            throw new RuntimeException("FILE OPEN/WRITE error: OUT-FILE", e);
        }
    }

    public static void main(String[] args) {
        new Sample1().run();
    }
}
