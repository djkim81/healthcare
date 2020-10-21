package com.skt.hrs.cmmn.csp;

import org.jdom2.Document;
import org.jdom2.Element;

import com.skt.hrs.cmmn.csp.vo.GetScbhInfoVo;
import com.skt.hrs.cmmn.csp.vo.HeaderVo;
import com.skt.hrs.cmmn.csp.vo.ResultVo;

/**
 * CSP 연동 Mail/쪽지/SMS 발송
 */
public class CspSender
{
    private final String name;

    private String[][]   paramLt;
    private String[]     contLt;

    public CspSender(String name)
    {
        this.name = name;
    }

    public CspSender init()
    {
        this.paramLt = null;
        this.contLt = null;

        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public String[][] getParamLt()
    {
        return this.paramLt;
    }

    public void setParamLt(String[][] paramLt)
    {
        this.paramLt = paramLt;
    }

    public String[] getContLt()
    {
        return this.contLt;
    }

    public void setContLt(String[] contLt)
    {
        this.contLt = contLt;
    }

    public ResultVo send()
    {
        return this.send(25000);
    }

    public ResultVo send(int timeout)
    {
        String URL = Config.domain + "/rest/" + this.name + "/" + Config.apiKey;

        ResultVo vo = new ResultVo();

        Document doc = CspUtil.getDocument(URL, this.paramLt, "POST", timeout);

        Element root = doc.getRootElement();
        Element head = root.getChild("HEADER");
        Element body = root.getChild("BODY");

        if (head != null) {
            vo.setHEADER(CspUtil.getHeader(head));
        }

        if (body != null) {
            vo.setBODY(getBody(vo.getHEADER(), body));
        }
        return vo;
    }

    // XML 내부에서 Body 정보를 담아옴
    private Object getBody(HeaderVo hvo, Element body)
    {
        GetScbhInfoVo vo = new GetScbhInfoVo();

        if (body == null) return vo;

        for (int i = 0; i < this.contLt.length; i++) {
            CspUtil.setData(vo, hvo, body, this.contLt[i]);
        }

        return vo;
    }
}
