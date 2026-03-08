package cobol.sample1;

/**
 * COBOL サブプログラム SUB1 に相当するクラス。
 *
 * COBOL 元定義（sub1.cbl）:
 *   PROGRAM-ID. SUB1.
 *   WORKING-STORAGE SECTION.
 *     01 EXT-WORK-AREA IS EXTERNAL.
 *       03 EXT-WORK-DATA PIC X(10).
 *   LINKAGE SECTION.
 *     01 PARAM-DATE.
 *       03 YYYY PIC X(4).
 *       03 MM   PIC X(2).
 *       03 DD   PIC X(2).
 *   PROCEDURE DIVISION USING PARAM-DATE.
 *
 * パラメータの渡し方:
 *   - CALL "SUB1" USING HOGE-DATE       → 参照渡し: 呼び出し側の HogeDate オブジェクトをそのまま渡す
 *   - CALL "SUB1" USING CONTENT HOGE-DATE → 値渡し: 呼び出し側がコピーを作成してから渡す
 *   どちらの場合も呼び出し側が判断するため、このメソッドは常に同じシグネチャを持つ。
 */
public class Sub1 {

    // IS EXTERNAL — SAMPLE1 と同一インスタンスを共有
    private static final ExternalArea extWorkArea = ExternalArea.INSTANCE;

    /**
     * PROCEDURE DIVISION USING PARAM-DATE に相当する。
     * paramDate は参照渡し（デフォルト）または値渡し（CONTENT）のどちらでも受け取れる。
     * 値渡しの場合は呼び出し側が HogeDate のコピーを生成してこのメソッドへ渡す。
     */
    public static void call(HogeDate paramDate) {
        // DISPLAY "  SUB1: PARAM-DATE: " PARAM-DATE
        CobolUtil.display("  SUB1: PARAM-DATE: ", paramDate.toCobolString());

        // MOVE "2027" TO YYYY
        paramDate.setYyyy("2027");

        // DISPLAY "  SUB1: EXT-WORK-DATA: " EXT-WORK-DATA
        CobolUtil.display("  SUB1: EXT-WORK-DATA: ", extWorkArea.getExtWorkData());

        // MOVE "SUB1" TO EXT-WORK-DATA
        extWorkArea.setExtWorkData("SUB1");

        // EXIT PROGRAM — メソッド終了で呼び出し元へ制御を返す
    }
}
