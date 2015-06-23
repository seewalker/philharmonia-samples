(ns
    ^{:doc "The (minimal) infrastructure needed for overtone-readiness of samples"
      :author "Alex Seewald" }
 sample-utils
  (:use   [overtone.live])
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defn path-to-described-samples
  [path]
  (let [relevant-fnames (filter (fn [fname] (-> (re-matches #".*.wav" fname) nil? not))
                                (map str (rest (file-seq (io/file path)))))
        feature-names (for [fname relevant-fnames]
                        (rest (map #(first (string/split % #".wav"))
                               (string/split fname #"_"))))]
    (zipmap feature-names (map load-sample relevant-fnames))))

(defn play-gen
    "A closure that produces a function ready to be used as if it were an overtone instrument. I abandoned using the definst infrastructure because the logistics were too complicated for my patience and this code similarly has a sense of defaults to handle partial input, and similarly produces a ugen."
  [described-inst defaults]
  (let [feature-names (keys defaults)
        inst-defaults (vals defaults)]
    (fn [& args]
      (let [descr (if (empty? args)
                    inst-defaults
                    (if (vector? (first args))
                      (first args)
                      (map-indexed #(if (contains? (first args) %2)
                                      (get (first args) %2)
                                      (get inst-defaults %1))
                                   feature-names)))]
        (if (contains? described-inst descr)
            (play-buf 1 (get described-inst descr))
            (println (format "%s that sample is not available" (str (vec descr)))))))))
