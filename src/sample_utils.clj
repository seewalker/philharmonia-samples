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
;time to resurrect this from the old commit, in order to incorperate this into autocorrect.
(defn featureset

  )
;there are many ways this can be done.
;check for correctness before doing anything? sounds reasonable performance-wise.
(defn possibly-autocorrect
  "This gets mapped over key-value pairs provided by the user.
   This checks for likely mis-spellings of key names, value names, not for the existence of a sample. Not all permutations are checked, that is.
   What kinds of errors should this throw?
  "
  [ [featurename featureval] defaults featureset {:keys max-distance correct-keynames? correct-values? modifiable-features}]
  (if (contains? (keys defaults) featurename)
    (if (contains? (get featureset featurename) featureval)
      featureval
      (if )
      )
      )
  (if (contains? modifiable-features featurename)

    (get defaults featurename)
    ))
(defn play-gen
  "A closure that produces a function ready to be used as if it were an overtone instrument. I abandoned using the definst infrastructure because the logistics were too complicated for my patience and this code similarly has a sense of defaults to handle partial input, and similarly produces a ugen.
  What is the first argument to play-buf? I am having it be '1' so far.
  "
  [described-inst defaults autocorrect-config]
  (let [feature-names (keys defaults)
        inst-defaults (vals defaults)
        map-handle (fn [the-map attempt-autocorrect?] (map-indexed #(if (contains? the-map (first %2))
                                                                       (get the-map (first %2))
                                                                       (possibly-autocorrect %2 defaults autocorrect-config))
                                                                  the-map))
        assemble-map (fn [])]
    (fn [& args]
      (let [descr (if (empty? args)
                    inst-defaults
                    (if (vector? (first args))
                      (first args)
                      (if (map? (first args))
                        (map-handle (first args) attempt-autocorrect?)
                        (map-handle (hash-map args) attempt-autocorrect?))))]
        (if (contains? described-inst descr)
            (play-buf 1 (get described-inst descr))
            (println (format "%s that sample is not available" (str (vec descr)))))))))
