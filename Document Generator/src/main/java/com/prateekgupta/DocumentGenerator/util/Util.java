package com.prateekgupta.DocumentGenerator.util;

import org.jsoup.Jsoup;

public class Util {
    public static String HTMLPreProcessor(String strHTML,String documentFormat) {
        String processedHTML = null;

        String WIDTH="width";

        String HEIGHT="height";

        int TABLE_WIDTH_LIMIT=700;

        int TABLE_ROW_HEIGHT_LIMIT_PDF=1025;

        int TABLE_DATA_HEIGHT_LIMIT_WORD=900;

        // Parsing HTML in String format
        org.jsoup.nodes.Document doc = Jsoup.parse(strHTML);

        // Setting the Output of the JSOUP to XML
        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        // Formatting <span> tags
        doc.select("span").forEach(t->{
            String styleAttribute = t.attr("style");
            if (Util.isNotBlank(t.attr("style")) && t.attr("style").contains("font-size") && styleAttribute.indexOf("pt", styleAttribute.indexOf("font-size") + 11)!=-1) {
                String width=styleAttribute.substring(styleAttribute.indexOf("font-size") + 11, styleAttribute.indexOf("pt", styleAttribute.indexOf("font-size") + 11));
                if (!width.contains(";")&& Float.parseFloat(width)>35)
                    t.attr("style",styleAttribute+" line-height: 45px;");
            }
        });

        // Formatting <img> tags
        doc.select("img").forEach(t -> {
            if (isNotBlank(t.attr(WIDTH)) && Float.parseFloat(t.attr(WIDTH)) > 600) {
                t.attr(WIDTH, "600");
            }
//            if (isNotBlank(t.attr(HEIGHT)) && Integer.parseInt(t.attr(HEIGHT)) > 300) {
//                t.attr(HEIGHT, "300");
//            }
        });

        // Formatting <table> tags
        doc.select("table").forEach(t -> {
            t.removeAttr(HEIGHT);
            String styleAttribute = t.attr("style");
            if (isNotBlank(t.attr("style")) && t.attr("style").contains(WIDTH) && styleAttribute.indexOf("px", styleAttribute.indexOf(WIDTH) + 7)!=-1) {
                String width=styleAttribute.substring(styleAttribute.indexOf(WIDTH) + 7, styleAttribute.indexOf("px", styleAttribute.indexOf(WIDTH) + 7));
                if (!width.contains(";")&& Float.parseFloat(width)>TABLE_WIDTH_LIMIT)
                    t.attr("style",styleAttribute.substring(0,styleAttribute.indexOf("width") + 7)+TABLE_WIDTH_LIMIT+styleAttribute.substring(styleAttribute.indexOf("px", styleAttribute.indexOf("width") + 7)));
            }
            if (isNotBlank(t.attr("style")) && t.attr("style").contains(HEIGHT) && styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8)!=-1) {
                String height=styleAttribute.substring(styleAttribute.indexOf(HEIGHT) + 8, styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));


                if (!height.contains(";")&& Integer.parseInt(height)>TABLE_ROW_HEIGHT_LIMIT_PDF) {
                    styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
                }
                else
                    styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
            }
            styleAttribute=styleAttribute.replace("border-style: none", "border-color:white");
            styleAttribute=styleAttribute.replace("border: none", "border-color:white");
            t.attr("style",styleAttribute);
        });


        // Formatting specific to PDF Document
        if (documentFormat.equalsIgnoreCase("pdf")) {

            // Formatting <p> tags
            doc.select("p").forEach(t->{
                if (t.nextElementSibling()!=null&&t.nextElementSibling().tagName().equals("p"))
                    t.attr("style",t.attr("style")+"margin-bottom:10px;");
            });

            // Formatting <h1> tags
            doc.select("h1").forEach(t->{
                if (t.attr("style").equals(""))
                    t.attr("style","font-weight: bold;font-size:24px");
            });

            // Formatting <tr> tags
            doc.select("tr").forEach(t->{
                t.removeAttr(HEIGHT);
                String styleAttribute = t.attr("style");
                if (isNotBlank(t.attr("style")) && t.attr("style").contains(HEIGHT) && styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8)!=-1) {
                    String height=styleAttribute.substring(styleAttribute.indexOf(HEIGHT) + 8, styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));

                    if (!height.contains(";")&& Integer.parseInt(height)>TABLE_ROW_HEIGHT_LIMIT_PDF) {
                        styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
                    }
                    else
                        styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
                }
                styleAttribute=styleAttribute.replace("border-style: none", "border-color:white");
                styleAttribute=styleAttribute.replace("border: none", "border-color:white");
                t.attr("style",styleAttribute);
            });

            // Formatting <td> tags
            doc.select("td").forEach(t->{
                t.removeAttr(HEIGHT);
                String styleAttribute = t.attr("style");
                if (isNotBlank(t.attr("style")) && t.attr("style").contains(HEIGHT) && styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8)!=-1) {
                    String height=styleAttribute.substring(styleAttribute.indexOf(HEIGHT) + 8, styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));


                    if (!height.contains(";")&& Integer.parseInt(height)>TABLE_ROW_HEIGHT_LIMIT_PDF) {
                        styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
                    }
                    else
                        styleAttribute=styleAttribute.replace(HEIGHT+": "+height+"px;","");
                }
                if (!(t.child(0).tagName().equals("p")&&t.child(0).child(0).tagName().equals("img")&&t.child(0).child(0).attr("src").equals("https://api-brdcmitsmbst.wolkenservicedesk.com/attachment/get_attachment_content?uniqueFileId=885020314271")))
                    styleAttribute+=" padding-left:5px;padding-right:5px;padding-top:0px !important;padding-bottom: 0px !important;";
                styleAttribute=styleAttribute.replace("border-style: none", "border-color:white");
                styleAttribute=styleAttribute.replace("border: none", "border-color:white");
                t.attr("style",styleAttribute);
            });
            processedHTML = doc.html().replaceAll("<hr />", "<hr/><br/>");
            processedHTML=processedHTML.replaceAll("\\*<", "\\\\*<");
            processedHTML=processedHTML.replaceAll("border-style: hidden;", "");
            System.out.println(processedHTML);
        }
        // Formatting specific to Word Document
        else {
            // Formatting <tr> tags
            doc.select("tr").forEach(t->{
                String styleAttribute = t.attr("style");
                if (isNotBlank(t.attr("style")) && t.attr("style").contains(HEIGHT) && styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8)!=-1) {
                    String height=styleAttribute.substring(styleAttribute.indexOf(HEIGHT) + 8, styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));

                    if (!height.contains(";")&& Integer.parseInt(height)>TABLE_DATA_HEIGHT_LIMIT_WORD) {
                        styleAttribute=styleAttribute.substring(0,styleAttribute.indexOf(HEIGHT) + 8)+TABLE_DATA_HEIGHT_LIMIT_WORD+styleAttribute.substring(styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));
                    }
                }
                styleAttribute=styleAttribute.replace("border-style: none", "border-color:white");
                styleAttribute=styleAttribute.replace("border: none", "border-color:white");
                t.attr("style",styleAttribute);
            });

            // Formatting <td> tags
            doc.select("td").forEach(t->{
                String styleAttribute = t.attr("style");
                if (isNotBlank(t.attr("style")) && t.attr("style").contains(HEIGHT) && styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8)!=-1) {
                    String height=styleAttribute.substring(styleAttribute.indexOf(HEIGHT) + 8, styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));

                    if (!height.contains(";")&& Integer.parseInt(height)>TABLE_DATA_HEIGHT_LIMIT_WORD) {
                        styleAttribute=styleAttribute.substring(0,styleAttribute.indexOf("height") + 8)+TABLE_DATA_HEIGHT_LIMIT_WORD+styleAttribute.substring(styleAttribute.indexOf("px", styleAttribute.indexOf(HEIGHT) + 8));
                    }
                }
                styleAttribute+=" padding-left:5px;padding-right:5px;";
                styleAttribute=styleAttribute.replace("border-style: none", "border-color:white");
                styleAttribute=styleAttribute.replace("border: none", "border-color:white");
                t.attr("style",styleAttribute);
            });
            System.out.println(doc.html());
            //doc.select("body").get(0).attr("style", "margin-left:32px;");
        }
        if (processedHTML != null) return processedHTML;
        else return doc.html();
    }
    
    static boolean isNotBlank(String str){
        if (str==null) return false;
        else return !str.equals("");
    }
}
