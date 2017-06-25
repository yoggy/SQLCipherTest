SQLCipherTest
====
ORMLiteとSQLiteCipherの組み合わせサンプル。

参考
----
* [GitHub - Andoctorey/ormlite-sqlcipher: Patched OrmLite Android library which encrypts persistent data transparently with SQLCipher](https://github.com/Andoctorey/ormlite-sqlcipher)
  * SQLCipherとOrmLiteを組み合わせて使うためのライブラリ
* [OrmLite - Lightweight Object Relational Mapping (ORM) Java Package](http://ormlite.com/)
  * Android環境で使えるSQLite用ORマッパー
  * [ISC license](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_9.html#License)
* [SQLCipher - Zetetic](https://www.zetetic.net/sqlcipher/sqlcipher-for-android/)
  * AndroidのSQLiteファイルを透過的に暗号化保存するライブラリ。内部でJNIが使われている
  * 暗号化・複合処理が入るため少し性能劣化するが、使ってみた感じでは実用的な速度で動作する印象
  * Community Editionは[BSDライセンス](https://www.zetetic.net/sqlcipher/license/)

使い方メモ
----
[Andoctorey/ormlite-sqlcipher](https://github.com/Andoctorey/ormlite-sqlcipher)を使用すると、次の修正を行うだけで既存のORMLiteを使用したアプリをSQLCipher対応にすることができる。

(1) build.gradleに以下を追加

    compile 'com.j256.ormlite.cipher:ormlite-sqlcipher:1.1@aar'
    compile 'com.j256.ormlite:ormlite-core:5.0'
    compile 'com.j256.ormlite:ormlite-android:5.0'
    compile 'net.zetetic:android-database-sqlcipher:3.5.7@aar'

(2) パッケージを変更する

    import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
    import android.database.sqlite.SQLiteDatabase;
    
    の代わりに
    
    import com.j256.ormlite.cipher.android.apptools.OrmLiteSqliteOpenHelper;
    import net.sqlcipher.database.SQLiteDatabase;
    
    を使用する。

(3) HelperクラスにgetPassword()メソッドを追加する
    - getPassword()はSQLCipherが暗号化用のパスワードを取得するために呼び出すメソッド。
    - サンプルではソースコードにパスワードをハードコーディングしているが、実際には端末毎に異なるパスワードを使用するのがよさげ

(4) アプリ起動時にSQLiteDatabase.loadLibs(context);を実行する
    - これを実行するとJNIライブラリを読み込む。実行しないとデータベースファイルのオープンsに失敗する…


Copyright and license
----
Copyright (c) 2017 yoggy

Released under the [MIT license](LICENSE.txt)