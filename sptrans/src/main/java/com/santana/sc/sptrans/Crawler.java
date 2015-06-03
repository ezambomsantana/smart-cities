package com.santana.sc.sptrans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Crawler {

	String cookie = "";

	public String getToken() throws Exception {

		String url = "http://api.olhovivo.sptrans.com.br/v0/Login/Autenticar?token=00bdda67079e3d5538df13c3f72531a5888c04649e4bcf39209a8b79f397b31a";
		URL c = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) c.openConnection();

		String urlParameters = "";

		connection = (HttpURLConnection) c.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		connection.setRequestProperty("Content-Length",
				"" + Integer.toString(urlParameters.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int status = connection.getResponseCode();
		// System.out.println(status);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			// System.out.println(inputLine);
		}

		Map<String, List<String>> headerFields = connection.getHeaderFields();

		Set<String> headerFieldsSet = headerFields.keySet();
		Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

		while (hearerFieldsIter.hasNext()) {

			String headerFieldKey = hearerFieldsIter.next();

			if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {

				List<String> headerFieldValue = headerFields
						.get(headerFieldKey);

				for (String headerValue : headerFieldValue) {

					String[] fields = headerValue.split(";\"s*");

					String cookieValue = fields[0];
					String expires = null;
					String path = null;
					String domain = null;
					boolean secure = false;

					// Parse each field
					for (int j = 1; j < fields.length; j++) {
						if ("secure".equalsIgnoreCase(fields[j])) {
							secure = true;
						} else if (fields[j].indexOf('=') > 0) {
							String[] f = fields[j].split("=");
							if ("expires".equalsIgnoreCase(f[0])) {
								expires = f[1];
							} else if ("domain".equalsIgnoreCase(f[0])) {
								domain = f[1];
							} else if ("path".equalsIgnoreCase(f[0])) {
								path = f[1];
							}
						}

					}

					cookie = cookieValue;

				}

			}

		}

		in.close();

		return null;
	}

	public List<Bus> getLinhas() throws Exception {
		
		List<Bus> buses = new ArrayList<Bus>();

		String onibus[] = new String[1];
		onibus[0] = "715M-10";
		
		//onibus[0] = "447P-10";


		for (int i = 0; i < onibus.length; i++) {

			String[] params = cookie.split("=");

			String[] value = params[1].split(";");
			// create cookie
			HttpCookie httpCookie = new HttpCookie(params[0], value[0]);

			// add cookie to CookieStore for a
			// particular URL
			String url = "http://api.olhovivo.sptrans.com.br/v0/Linha/Buscar?termosBusca="
					+ onibus[i];

			URL u = new URL(url);
			// cookieJar.add(u.toURI(), cookie);

			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();

			connection.setRequestProperty("Cookie", cookie);

			connection.toString();

			int status = connection.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			String total = "";

			while ((inputLine = in.readLine()) != null) {
				total = total + inputLine;
			}
			
			JSONObject jsonObject;
			JSONParser parser = new JSONParser();

			JSONArray units = (JSONArray) parser.parse(total);

			Iterator unitsIterator = units.iterator();
			while (unitsIterator.hasNext()) {
				jsonObject = (JSONObject) unitsIterator.next();

				Long codigo = (Long) jsonObject.get("CodigoLinha");
				String letreiro = (String) jsonObject.get("Letreiro");
				String denominacaoTPTS = (String) jsonObject.get("DenominacaoTPTS");
				String denominacaoTSTP = (String) jsonObject.get("DenominacaoTSTP");
				Long sentido = (Long) jsonObject.get("Sentido");

				url = "http://api.olhovivo.sptrans.com.br/v0/Posicao?codigoLinha=" + codigo;
				u = new URL(url);
				// cookieJar.add(u.toURI(), cookie);

				connection = (HttpURLConnection) u.openConnection();

				connection.setRequestProperty("Cookie", cookie);

				connection.toString();

				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				inputLine = "";
				total = "";

				while ((inputLine = in.readLine()) != null) {
					total = total + inputLine;

				}

				JSONObject units2 = (JSONObject) parser.parse(total);

				String hr = (String) units2.get("hr");
				units = (JSONArray) units2.get("vs");

				Iterator it = units.iterator();
				int t = 0;
				while (it.hasNext()) {
					t++;
					System.out.println("busao: " + t);
					System.out.println(jsonObject);
					jsonObject = (JSONObject) it.next();
					Double py = (Double) jsonObject.get("py");
					Double px = (Double) jsonObject.get("px");

					String denomicao = sentido == 1 ? denominacaoTPTS
							: denominacaoTSTP;
					
					Bus bus = new Bus();
					bus.setCode((Integer.parseInt((String) jsonObject.get("p"))));
					bus.setLat(py);
					bus.setLon(px);
					bus.setName(denomicao);
					bus.setLetters(letreiro);
					
					buses.add(bus);

					System.out.println("\"Codigo: " + bus.getCode()
							+ "\" Letreiro: \"" + letreiro + "\" Denominacao: "
							+ denomicao + " Hora: " + hr + " lat: " + py
							+ "long: " + px);
				}

			}

			in.close();
		}

		return buses;
	}

}
