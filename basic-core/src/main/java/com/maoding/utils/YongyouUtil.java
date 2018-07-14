package com.maoding.utils;
/*     */ 
/*     */ import java.security.SignatureException;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;

/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;

/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.http.Header;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YongyouUtil
/*     */ {
/*     */   public static String sign(String secret, String path, Header[] headers, String bodys)
/*     */     throws JSONException
/*     */   {
/*  49 */     Map<String, String> headerMap = new HashMap();
/*  50 */     Header[] arrayOfHeader; int j = (arrayOfHeader = headers).length; for (int i = 0; i < j; i++) { Header header = arrayOfHeader[i];
/*  51 */       headerMap.put(header.getName(), header.getValue());
/*     */     }
/*     */     
/*  54 */     return signSha1(secret, null, path, headerMap, null, bodys, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Map<String, String> jsonString2Map(String jsonString)
/*     */     throws JSONException
/*     */   {
/*  64 */     if (jsonString == null) {
/*  65 */       return null;
/*     */     }
/*  67 */     JSONObject json = new JSONObject(jsonString);
/*  68 */     Map<String, String> map = new HashMap();
/*     */     
/*     */ 
/*  71 */     Iterator<String> it = json.keys();
/*  72 */     while (it.hasNext()) {
/*  73 */       String key = (String)it.next();
/*  74 */       map.put(key, json.getString(key));
/*     */     }
/*     */     
/*  77 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String signSha1(String secret, String method, String path, Map<String, String> headers, Map<String, String> querys, String bodys, List<String> signHeaderPrefixList)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       return hmacSHA1(buildStringToSign(method, path, headers, querys, bodys, signHeaderPrefixList), secret);
/*     */     } catch (Exception e) {
/* 100 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String sign(String secret, String method, String path, Map<String, String> headers, Map<String, String> querys, String bodys, List<String> signHeaderPrefixList)
/*     */   {
/*     */     try
/*     */     {
/* 122 */       Mac hmacSha1 = Mac.getInstance("HmacSHA1");
/* 123 */       byte[] keyBytes = secret.getBytes("UTF-8");
/* 124 */       hmacSha1.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA1"));
/*     */       
/* 126 */       return new String(Base64.encodeBase64(
/* 127 */         hmacSha1.doFinal(buildStringToSign(method, path, headers, querys, bodys, signHeaderPrefixList)
/* 128 */         .getBytes("UTF-8"))), 
/* 129 */         "UTF-8");
/*     */     } catch (Exception e) {
/* 131 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String buildStringToSign(String method, String path, Map<String, String> headers, Map<String, String> querys, String bodys, List<String> signHeaderPrefixList)
/*     */   {
/* 148 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 150 */     if (method != null) {
/* 151 */       sb.append(method.toUpperCase()).append("\n");
/*     */     }
/* 153 */     sb.append(buildHeaders(headers));
/* 154 */     sb.append(buildResource(path, querys));
/*     */     
/* 156 */     if (bodys != null) {
/* 157 */       sb.append("\n").append(bodys);
/*     */     }
/* 159 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String buildResource(String path, Map<String, String> querys)
/*     */   {
/* 171 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 173 */     if (!StringUtils.isBlank(path)) {
/* 174 */       int index = path.indexOf("/apis/");
/* 175 */       if (index > 0)
/* 176 */         path = path.substring(index);
/* 177 */       sb.append(path);
/*     */     }
/* 179 */     Map<String, String> sortMap = new TreeMap();
/* 180 */     if (querys != null) {
/* 181 */       for (Entry<String, String> query : querys.entrySet()) {
/* 182 */         if (!StringUtils.isBlank((String)query.getKey())) {
/* 183 */           sortMap.put((String)query.getKey(), (String)query.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 188 */     StringBuilder sbParam = new StringBuilder();
/* 189 */     for (Object item : sortMap.entrySet()) {
/* 190 */       if (!StringUtils.isBlank((String)((Entry)item).getKey())) {
/* 191 */         if (sbParam.length() > 0) {
/* 192 */           sbParam.append("&");
/*     */         }
/* 194 */         sbParam.append((String)((Entry)item).getKey());
/* 195 */         if (!StringUtils.isBlank((String)((Entry)item).getValue())) {
/* 196 */           sbParam.append("=").append((String)((Entry)item).getValue());
/*     */         }
/*     */       }
/*     */     }
/* 200 */     if (sbParam.length() > 0) {
/* 201 */       sb.append("?");
/* 202 */       sb.append(sbParam);
/*     */     }
/*     */     
/* 205 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String buildHeaders(Map<String, String> headers, List<String> signHeaderPrefixList)
/*     */   {
/* 218 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 220 */     if (signHeaderPrefixList != null) {
/* 221 */       signHeaderPrefixList.remove("X-Ca-Signature");
/* 222 */       signHeaderPrefixList.remove("Accept");
/* 223 */       signHeaderPrefixList.remove("Content-MD5");
/* 224 */       signHeaderPrefixList.remove("Content-Type");
/* 225 */       signHeaderPrefixList.remove("Date");
/* 226 */       Collections.sort(signHeaderPrefixList);
/* 227 */       if (headers != null) {
/* 228 */         Map<String, String> sortMap = new TreeMap();
/* 229 */         sortMap.putAll(headers);
/* 230 */         StringBuilder signHeadersStringBuilder = new StringBuilder();
/* 231 */         for (Entry<String, String> header : sortMap.entrySet()) {
/* 232 */           if (isHeaderToSign((String)header.getKey(), signHeaderPrefixList)) {
/* 233 */             sb.append((String)header.getKey());
/* 234 */             sb.append(":");
/* 235 */             if (!StringUtils.isBlank((String)header.getValue())) {
/* 236 */               sb.append((String)header.getValue());
/*     */             }
/* 238 */             sb.append("\n");
/* 239 */             if (signHeadersStringBuilder.length() > 0) {
/* 240 */               signHeadersStringBuilder.append(",");
/*     */             }
/* 242 */             signHeadersStringBuilder.append((String)header.getKey());
/*     */           }
/*     */         }
/* 245 */         headers.put("X-Ca-Signature-Headers", signHeadersStringBuilder.toString());
/*     */       }
/*     */     }
/*     */     
/* 249 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String buildHeaders(Map<String, String> headers)
/*     */   {
/* 262 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 264 */     if (headers != null) {
      Map<String, String> sortMap = new TreeMap(new Comparator() {
		@Override
		public int compare(Object key1, Object key2) {
			// TODO Auto-generated method stub
			return ((String) key1).compareTo((String) key2);
		}
      });
/* 271 */       sortMap.putAll(headers);
/* 272 */       StringBuilder signHeadersStringBuilder = new StringBuilder();
/* 273 */       for (Entry<String, String> header : sortMap.entrySet()) {
/* 274 */         sb.append((String)header.getKey());
/* 275 */         sb.append(":");
/* 276 */         if (!StringUtils.isBlank((String)header.getValue())) {
/* 277 */           sb.append((String)header.getValue());
/*     */         }
/* 279 */         sb.append("\n");
/* 280 */         if (signHeadersStringBuilder.length() > 0) {
/* 281 */           signHeadersStringBuilder.append(",");
/*     */         }
/* 283 */         signHeadersStringBuilder.append((String)header.getKey());
/*     */       }
/* 285 */       headers.put("X-Ca-Signature-Headers", signHeadersStringBuilder.toString());
/*     */     }
/*     */     
/* 288 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isHeaderToSign(String headerName, List<String> signHeaderPrefixList)
/*     */   {
/* 295 */     if (StringUtils.isBlank(headerName)) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     if (headerName.startsWith("X-Ca-")) {
/* 300 */       return true;
/*     */     }
/*     */     
/* 303 */     if (signHeaderPrefixList != null) {
/* 304 */       for (String signHeaderPrefix : signHeaderPrefixList) {
/* 305 */         if (headerName.equalsIgnoreCase(signHeaderPrefix)) {
/* 306 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 311 */     return false;
/*     */   }
/*     */   
/*     */   public static String hmacSHA1(String data, String key) throws SignatureException
/*     */   {
/* 316 */     String HMAC_SHA1_ALGORITHM = "HmacSHA1";
/*     */     String result;
/*     */     try
/*     */     {
/* 320 */       SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
/*     */       
/*     */ 
/* 323 */       Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
/* 324 */       mac.init(signingKey);
/*     */       
/*     */ 
/* 327 */       byte[] rawHmac = mac.doFinal(data.getBytes());
/*     */       
/*     */  
/* 330 */       result = new String(Base64.encodeBase64(rawHmac), "UTF-8");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */      
/* 335 */       throw new SignatureException("Failed to generate HMAC : " + e.getMessage()); }
/*     */    
/* 337 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Users\sandy\Desktop\demoSDK\lib\iovp_apilink.jar
 * Qualified Name:     util.SignUtil
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */