package upem.tasksAnd.start.models;

public class Attachement {
//atachement id equals task id or subtask-id
private int attachementid;
private String attachmentPath;
private String getAttachmentType;
private String fileName;

    public int getAttachementid() {
        return attachementid;
    }

    public void setAttachementid(int attachementid) {
        this.attachementid = attachementid;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getGetAttachmentType() {
        return getAttachmentType;
    }

    public void setGetAttachmentType(String getAttachmentType) {
        this.getAttachmentType = getAttachmentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
