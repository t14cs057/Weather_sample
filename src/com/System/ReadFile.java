// CSV ファイルを読み込む
// アメダスにおける各都道府県の情報を取得する
package com.System;
import java.io.*;
import java.util.Date;
import java.util.List;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.jdo.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
import com.Login.PMF;
import com.Data.*;

public class ReadFile{
	public ReadFile(){
		try {
			// 読み込むファイルの置き場所
			inputFile("csv/amedas.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void inputFile(String str) throws IOException{

		PersistenceManager pm = PMF.get().getPersistenceManager();			
		Query query = pm.newQuery(Amedas.class);
		query.setFilter("fuken_name == " + "'山梨'");
		List<Amedas> amedases = (List<Amedas>) query.execute();
		// アメダスのファイルが一度も読み込まれていないとき
		// ファイル読み込みの処理を行なう
		if( amedases.size() == 0 ){
			// 読み込むファイルを new する
			File file = new File(str);

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			// 開始2行は無視
			br.readLine();
			br.readLine();

			String line = null;
			try {
				// ファイルの最後の行まで繰り返す
				while ((line = br.readLine()) != null) {
					// 振興局, 府県番号, 観測所名, カタカナ名, 地点コード
					// カンマ(,)で区切って配列に格納
					String[] split = line.split(",");
					// 気象データの登録
					Amedas amedas = new Amedas( split[0], Integer.parseInt(split[1]), split[2], split[3], Integer.parseInt(split[4]) );

					pm.makePersistent(amedas); // Amedas カインドにエンティティを追加
				}
				br.close();
			} finally {
				pm.close();
			}

		}else{ // Amedas データが一度でも読み込まれて入れば
			// 何もせず処理を終了する
			pm.close();
			return;
		}
	}

}