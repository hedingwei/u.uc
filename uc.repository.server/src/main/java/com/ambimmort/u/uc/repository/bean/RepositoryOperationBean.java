/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.u.uc.repository.bean;

/**
 *
 * @author 定巍
 */
public class RepositoryOperationBean {
    private int messageNo;
    private int operation;
    private String content;

    public int getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(int messageNo) {
        this.messageNo = messageNo;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UpdateOperation{" + "messageNo=" + messageNo + ", operation=" + operation + ", content=" + content + '}';
    }
    
    
}
