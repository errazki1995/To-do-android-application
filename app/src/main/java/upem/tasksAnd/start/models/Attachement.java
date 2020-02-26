package upem.tasksAnd.start.models;

public class Attachement {
//atachement id equals task id or subtask-id

private int attachementid;
private String attachmentPath;
private String attachmentType;
private String taskid;

public Attachement(String attachmentPath,String attachType){
    this.attachmentPath=attachmentPath;
    this.attachmentType= attachType;
    this.taskid = taskid;
}
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
        return attachmentType;
    }

    public void setGetAttachmentType(String getAttachmentType) {
        this.attachmentType = getAttachmentType;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
