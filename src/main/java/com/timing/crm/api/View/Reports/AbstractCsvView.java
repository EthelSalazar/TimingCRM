package com.timing.crm.api.View.Reports;

import org.springframework.web.servlet.view.AbstractView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCsvView extends AbstractView {

    private static final String CSV_CONTENT_TYPE = "text/csv";
    private String url;


    public AbstractCsvView() {
        setContentType(CSV_CONTENT_TYPE);
    }


    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }


    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setContentType(getContentType());
        buildCsvDocument(map, httpServletRequest, httpServletResponse);
    }


    protected abstract void buildCsvDocument(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
        throws Exception;

}
