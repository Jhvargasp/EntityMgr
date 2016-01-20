<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="ISO-8859-1"%>
<%!
public void copy(File src, File dst) throws IOException {
    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(dst);
    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
    }
    in.close();
    out.close();
}
 %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String f=request.getParameter("f");
String folderTmp="c:/1 pasos/otherFolder/";
File original=new File(f);
System.out.println("Exist file? "+f+" > "+original.exists());
boolean retorno=true;
if(!original.exists()){
	f=folderTmp+original.getName();
	original=new File(f);
	System.out.println("Exist file? "+f+" > "+original.exists());
	if(original.exists()){
		copy(new File(f),new File(request.getParameter("f")));
		System.out.println("File Copied...");
	}	else{
		retorno=false;
	}
}
out.println(retorno);
%>