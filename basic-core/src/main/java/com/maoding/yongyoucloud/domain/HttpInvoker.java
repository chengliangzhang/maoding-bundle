package com.maoding.yongyoucloud.domain;

import com.maoding.utils.YongyouUtil;
import com.maoding.yongyoucloud.config.EnterpriseSearchApicode;
import com.maoding.yongyoucloud.config.PoolConnManagerConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.Map;



@Component("httpinvoker")
/*     */ public class HttpInvoker extends PoolConnManagerConfig
/*     */ {
/*     */   CloseableHttpClient httpClient;
/*     */   PoolingHttpClientConnectionManager poolConnManager;
/*     */

/*     */   public HttpInvoker() throws Exception
/*     */   {
/*  51 */     init();
/*     */   }
/*     */
/*     */   public void init() throws Exception
/*     */   {
/*  80 */     SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()
/*     */     {
/*     */       public boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
/*  83 */         return true;
/*     */       }
/*  85 */     }).build();
/*  86 */     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
/*  87 */     Registry<Object> socketFactoryRegistry = RegistryBuilder.create()
/*  88 */       .register("http", org.apache.http.conn.socket.PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
/*  89 */     poolConnManager = new PoolingHttpClientConnectionManager(((Registry)socketFactoryRegistry));
/*  91 */     poolConnManager.setMaxTotal(maxTotal);
/*  93 */     poolConnManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
/*  94 */     SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(soTimeout).build();
/*  95 */     poolConnManager.setDefaultSocketConfig(socketConfig);
/*  97 */     RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
/*  98 */       .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
/*  99 */     httpClient = org.apache.http.impl.client.HttpClients.custom().setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig)
/* 100 */       .build();
/* 101 */     if (poolConnManager.getTotalStats() != null) {
/* 102 */       System.out.println("now client pool " + poolConnManager.getTotalStats().toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void destoy() {
/*     */     try {
/* 108 */       if (httpClient != null)
/* 109 */         httpClient.close();
/* 110 */       if (poolConnManager != null)
/* 111 */         poolConnManager.close();
/*     */     } catch (IOException e) {
/* 113 */       throw new RuntimeException("Error in destroy HttpClient : ", e);
/*     */     }
/*     */   }

	/*     */
/*     */   public String invoker(String testUrl, Map<String, String> params)
/*     */   {
/* 121 */     return getOrDeleteMethod(testUrl, params, "GET", EnterpriseSearchApicode.header);
/*     */   }
/*     */   
/*     */   public String invoker(String testUrl, Map<String, String> params, String methodType, Map<String, String> header)
/*     */   {
/* 119 */     if (("POST".equalsIgnoreCase(methodType)) || ("PUT".equalsIgnoreCase(methodType)))
/* 120 */       return putOrPostMethod(testUrl, params, methodType, header);
/* 121 */     return getOrDeleteMethod(testUrl, params, methodType, header);
/*     */   }
/*     */   
/*     */   private String getOrDeleteMethod(String testUrl, Map<String, String> params, String methodType, Map<String, String> header)
/*     */   {
/* 126 */     String result = null;
/*     */     try
/*     */     {
/* 132 */       testUrl = testUrl + "?" + urlencode(params);
/* 134 */       HttpRequestBase method;
			if ("GET".equalsIgnoreCase(methodType)) {
/* 135 */         method = new HttpGet(testUrl);
/*     */       } else {
/* 137 */         method = new HttpDelete(testUrl);
/*     */       }
/* 139 */       StringBuilder headersToSign = new StringBuilder();
/* 141 */       if (header != null) {
/* 142 */         String appsecret = (String)header.get("appsecret");
/* 143 */         header.remove("appsecret");
/* 144 */         for (Map.Entry<String, String> map : header.entrySet()) {
/* 145 */           String key = "appkey".equals(map.getKey()) ? "X-Ca-key" : (String)map.getKey();
/* 146 */           String value = (String)map.getValue();
/* 147 */           method.addHeader(key, value);
/* 148 */           if (headersToSign.length() > 0)
/* 149 */             headersToSign.append(";");
/* 150 */           headersToSign.append(key);
/*     */         }
/* 153 */         if ("appkey".equalsIgnoreCase((String)header.get("authoration"))) {
/* 154 */           String signature = YongyouUtil.sign(appsecret, testUrl,
/* 155 */             method.getAllHeaders(), null);
/* 156 */           method.addHeader("X-Ca-Signature", signature);
/* 157 */           method.addHeader("X-Ca-Signature-Header", headersToSign.toString());
/*     */         }
/*     */       }
/* 161 */       if (httpClient == null) {
/* 162 */         init();
/*     */       }
/* 164 */       HttpResponse httpResponse = httpClient.execute(method);
/*     */       
/* 166 */       int statusCode = httpResponse.getStatusLine().getStatusCode();
/* 167 */       if (statusCode != 200) {
/* 168 */         HttpEntity entity = httpResponse.getEntity();
/* 169 */         String errMsg = EntityUtils.toString(entity);
/* 170 */         result = "error status : " + statusCode + ", " + errMsg;
/*     */       } else {
/* 172 */         HttpEntity entity = httpResponse.getEntity();
/* 173 */         result = EntityUtils.toString(entity);
/*     */       }
/* 175 */       method.releaseConnection();
/*     */     } catch (Exception e) {
/* 177 */       result = e.getMessage();
/* 178 */       if ((result == null) && (e.getCause() != null)) {
/* 179 */         result = e.getCause().getMessage();
/*     */       }
/* 181 */       if (result == null) {
/* 182 */         result = "Unknown exception";
/*     */       }
/*     */     }
/* 185 */     return result;
/*     */   }
/*     */   
/*     */   private String putOrPostMethod(String testUrl, Map<String, String> params, String methodType, Map<String, String> header) {
/* 189 */     String result = null;
/*     */     try {
/*     */       HttpEntityEnclosingRequestBase method;
/* 193 */       if ("PUT".equalsIgnoreCase(methodType)) {
/* 194 */         method = new HttpPut(testUrl);
/*     */       } else {
/* 196 */         method = new HttpPost(testUrl);
/*     */       }
/* 198 */       JSONObject paramsJson = new JSONObject(params);
/*     */       
/* 200 */       HttpEntity requestEntity = new org.apache.http.entity.StringEntity(paramsJson.toString(), ContentType.APPLICATION_JSON);
/* 201 */       method.setEntity(requestEntity);
/*     */       
/*     */ 
/* 204 */       StringBuilder headersToSign = new StringBuilder();
/* 205 */       if (header != null) {
/* 206 */         String appsecret = (String)header.get("appsecret");
/* 207 */         header.remove("appsecret");
/* 208 */         for (Map.Entry<String, String> map : header.entrySet()) {
/* 209 */           String key = "appkey".equals(map.getKey()) ? "X-Ca-key" : (String)map.getKey();
/* 210 */           String value = (String)map.getValue();
/* 211 */           method.addHeader(key, value);
/* 212 */           if (headersToSign.length() > 0)
/* 213 */             headersToSign.append(";");
/* 214 */           headersToSign.append(key);
/*     */         }
/*     */         
/*     */ 
/* 218 */         if ("appkey".equalsIgnoreCase((String)header.get("authoration"))) {
/* 219 */           String signature = YongyouUtil.sign(appsecret, testUrl, 
/* 220 */             method.getAllHeaders(), paramsJson.toString());
/* 221 */           method.addHeader("X-Ca-Signature", signature);
/* 222 */           method.addHeader("X-Ca-Signature-Header", headersToSign.toString());
/*     */         }
/*     */       }
/*     */       
/* 226 */       if (httpClient == null) {
/* 227 */         init();
/*     */       }
/* 229 */       HttpResponse httpResponse = httpClient.execute(method);
/*     */       
/* 231 */       int statusCode = httpResponse.getStatusLine().getStatusCode();
/* 232 */       if (statusCode != 200) {
/* 233 */         HttpEntity entity = httpResponse.getEntity();
/* 234 */         String errMsg = EntityUtils.toString(entity);
/* 235 */         result = "error status : " + statusCode + ", " + errMsg;
/*     */       } else {
/* 237 */         HttpEntity entity = httpResponse.getEntity();
/* 238 */         result = EntityUtils.toString(entity);
/*     */       }
/* 240 */       method.releaseConnection();
/*     */     } catch (Exception e) {
/* 242 */       result = e.getMessage();
/* 243 */       if ((result == null) && (e.getCause() != null)) {
/* 244 */         result = e.getCause().getMessage();
/*     */       }
/* 246 */       if (result == null) {
/* 247 */         result = "Unknown exception";
/*     */       }
/*     */     }
/* 250 */     return result;
/*     */   }
/*     */   
/*     */   public static String urlencode(Map<String, String> data) {
/* 254 */     StringBuilder sb = new StringBuilder();
/* 255 */     for (Map.Entry<String, String> i : data.entrySet()) {
/*     */       try {
/* 257 */         if ("".equals(i.getKey())) {
/* 258 */           sb.append(URLEncoder.encode((String)i.getValue(), "UTF-8"));
/*     */         } else {
/* 260 */           sb.append((String)i.getKey()).append("=").append(URLEncoder.encode((String)i.getValue(), "UTF-8")).append("&");
/*     */         }
/*     */       } catch (UnsupportedEncodingException e) {
/* 263 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 266 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\sandy\Desktop\demoSDK\lib\iovp_apilink.jar
 * Qualified Name:     http.HttpInvoker
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */