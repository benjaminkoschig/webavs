package globaz.webavs.common;

import lombok.Data;

@Data
public class VersionProperties {
    private String timestamp;
    private String projectName;
    private String projectVersion;
    private String projectVersionTechnical;
    private String versionServicePack;
    private String date;
    private String buildNumber;
    private String buildNumberUnique;
    private String closestTagName;
    private String commitId;
    private String commitIdAbbrev;
    private String scmBranch;
    private String commitIdDescribe;
    private String commitIdDescribeShort;
}
