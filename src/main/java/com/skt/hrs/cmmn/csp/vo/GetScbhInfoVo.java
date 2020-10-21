package com.skt.hrs.cmmn.csp.vo;

public class GetScbhInfoVo
{
    private String RETURN;
    private String UUID;

    public String getRETURN()
    {
        return this.RETURN;
    }

    public void setRETURN(String rETURN)
    {
        RETURN = rETURN;
    }

    public String getUUID()
    {
        return this.UUID;
    }

    public void setUUID(String uUID)
    {
        UUID = uUID;
    }

    @Override
    public String toString()
    {
        return "GetScbhInfoVo [RETURN=" + RETURN + ", UUID=" + UUID + "]";
    }

}
