package mbti.mbti_test.domain;

public enum MbtiList {
    ENFJ("범고래"),
    ENTJ("흰수염고래"),
    ENFP("상어고래");

    private String whaleName;

    MbtiList(String whaleName) {
        this.whaleName = whaleName;
    }

    public String whaleNameMethod() { return this.whaleName; }
}
