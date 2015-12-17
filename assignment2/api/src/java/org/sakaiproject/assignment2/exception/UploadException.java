package org.sakaiproject.assignment2.exception;

public class UploadException extends AssignmentException
{
    public UploadException(String msg)
    {
        super(msg);
    }

    public UploadException(String msg, Throwable t)
    {
        super(msg, t);
    }
}