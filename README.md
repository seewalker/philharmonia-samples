# What does this project do for you?

This project provides one module per sample instrument. Each module provides a function, of the same name as the instrument, which returns a ugen suitable for use with overtone. The 'definst' infrastructure is not used because these sampled instruments have many variants, e.g. vibrato or non-vibrato, of the same note.

Because certain combinations of the features may be absent for

# How to use the sampled instrument functions

Let's use the example of a cello, because all the samples instruments are similar to this.

##loading samples

(use 'sampled-cello)
;assuming philharmonia-samples/src is in your classpath.
(:require [sampled-cello :refer [cello celloi]])
;all the sampled instrument files have similar information in them, sharing the same name, so you should probably 'require' rather than 'use' it, and you likely don't care about those other names, so you could just refer the ugen producing function (cello) and the instrument-like function (celloi).

Loading all the samples associated with an instrument can produce a var taking up as little as 21MB, in the case of the mandolin, and as much as 169 MB, in the case of the violin. For this reason, the samples are grouped into separate files by instrument.

## calling samples
`(cello)`
;produces a ugen playing the default sample.

`(demo (cello))`
;will make your speakers play the default sample.

`(cello {:note "As3" :loudness "piano" :style "vibrato" :duration "025"])`
; you can specify all parameters in a map.

`(cello {:loudness "forte" :style "vibrato" :duration "025" :note "G4"])`
; order does not matter in the map, so this will also produce a sound.

`(cello {:note "As3"})`
;you can specify only the parameters you wish, and the default value for each unspecified parameter gets filled in.

`(cello :note "As3" :style "vibrato")`
;or, since overtone is meant for live coding, you can save some precious keystrokes by passing keys and values instead of an explicit map.

`(cello {:note 60})`
;passing an integer will make the value interpreted as a midi note, rather than a note name.

`(cello 60)`
;'note' is arguably the most significant feature, so a single scalar argument will be interpreted as a note.

`(cello` ["As3" "piano" "vibrato"])
;when passing a vector, you must specify all features.

`(celloi)`

`(celloi :note 58)`
; if your use case lends itself to functions that produce sounds (rather than ugens) when called, you can use function with 'i' as a suffix, which is otherwise identical in how it's used.

Because `(cello)` produces a ugen, it could be used like so:
`defcgen `

## error handling

If you specify a parameter that does not exist, the default value is used.
If you specify a value that a parameter cannot take, autocorrection is attempted. If no suitable correction, the default value is used.
If you specify a combination of allowed and parameters that has no corresponding sample ...

## customizations

- You can scale up or scale back how eagerly misspellings are autocorrected for each parameter. The `distance-maxes` var in each sampled instrument file has keys corresponding to the parameters, and values corresponding to the maximum string distance between allowed values and values that will be corrected. Note, these values can be set to '0' if you do not want any autocorrection.
- You can change what the default parameters for an instrument are by changing the `defaults` var.

# How to get things going:

Overtone requires ".wav" files, but the philharmonia website provides mp3s. I've gone through the work of converting them, and you can download the samples from [](https://drive.google.com/folderview?id=0B7GoGDjZUyZ5flIxOXNPN2VjdXl2ZXpzTGRQTE9OU3pJYmV4Vlg3VFZRd3hhek5lelNiUVU&usp=sharing)

The library expects a directory structure with naming conventions like what you can download at that link. The sample's root directory can be anywhere on your filesystem, and the samples can be used if the `sampleroot` var in `sample_utils.clj` file points to it.

# Welcome Changes

- The 1.0.0 version of this project might involve more precise names for the instrument features. For example, I chose "loudness" as the name for the class of features like "piano" and "forte", and "style" as the name for features like "arco-normal", but this might not be the terminology musicians want to work with.

- Of course, more samples and a better multi-gigabye public storage solution would be a plus.
