(defproject moody "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
    :dependencies [[org.clojure/clojure "1.9.0"]
                   [org.clojure/data.csv "0.1.4"]
                   [org.clojure/data.json "0.2.6"]
                   [clj-http "3.9.1"]]
  :main ^:skip-aot moody.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
