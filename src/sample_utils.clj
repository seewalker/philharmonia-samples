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
  [sample-descriptions defaults]
  (zip-map (keys defaults) (map (comp vec set) (transpose sample-descriptions))))

(defn possibly-autocorrect
  "This gets mapped over key-value pairs provided by the user.
   This checks for likely mis-spellings of key names, value names, not for the existence of a sample. Not all permutations are checked, that is.
   In the case of no correction, just print an error message.
   Right now the assumption is made that if the featurename is being corrected, the featureval is correct.
   Precondition - the guess is not already correct.
  "
  [ [featurename featureval] defaults featureset {:keys max-distance correct-keynames? correct-values? modifiable-features}]
  (let [kds (for [allowed-featurename (keys defaults)]
               {:n allowed-featurename :d (edit-distance featurename allowed-featurename)})
        kminimum (first (sort-by :d kds))
        vds (for [allowed-valuename (get featureset featurename)]
              {:n allowed-valuename :d (edit-distance featureval allowed-valuename)})
        vminimum (first (sort-by :d vds))
        ]
    (if (contains? (keys defaults) featurename) ;the featurename is valid.
     (if (contains? (get featureset featurename) featureval) ;the featureval is valid.
       featureval
       (if (and (string? featureval) correct-values? (contains? modifiable-features featurename)) ;the featureval is modifyablw
         (if (< (:d vminimum) max-distance)
           (:n vminimum)
           (println (format "attempted to correct feature %s, but no suitable correction found" featurename)))
         (println (format "unmodifyable feature %s will not undergo correction" featurename))))
     (if (and correct-keynames? (< (:d kminimum) max-distance))            ;featurename is not valid
       (:n kminimum)
      )
     )))

(defn play-gen
  "A closure that produces a function ready to be used as if it were an overtone instrument. I abandoned using the definst infrastructure because the logistics were too complicated for my patience and this code similarly has a sense of defaults to handle partial input, and similarly produces a ugen.
  What is the first argument to play-buf? I am having it be '1' so far.
  "
  [described-inst defaults autocorrect-config]
  (let [feature-names (keys defaults)
        inst-defaults (vals defaults)
        map-handle (fn [the-map attempt-autocorrect?] (for [[feature-name feature-val] the-map] ;wait, but this does not account for unprovided features.
                                                       (possibly-autocorrect feature-name feature-val defaults )))
    (fn [& args]
      (let [descr (if (empty? args)
                    inst-defaults
                    (if (vector? (first args))
                      (first args)
                      (if (map? (first args))
                        (map-handle (first args) attempt-autocorrect?) ;map-handle has to sort keys into a vector.
                        (map-handle (hash-map args) attempt-autocorrect?))))]
        (if (contains? described-inst descr)
            (play-buf 1 (get described-inst descr))
            (println (format "%s that sample is not available" (str (vec descr)))))))))
