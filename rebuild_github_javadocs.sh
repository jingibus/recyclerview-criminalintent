#!/bin/bash

set -e

INITIAL_COMMIT=$(echo $( git symbolic-ref HEAD 2> /dev/null || git rev-parse --short HEAD 2> /dev/null ) | sed "s#refs/heads/##")
PUBLISH_BRANCH=gh-pages
JAVADOCS=recyclerview-multiselect/build/docs/javadoc
TEMP_FOLDER=/tmp/$(basename $JAVADOCS).$$.tmp

rm -rf $TEMP_FOLDER
mkdir $TEMP_FOLDER

echo 'Building javadocs...'

./gradlew generateReleaseJavadoc

pandoc README.md -o $TEMP_FOLDER/index.html

mv $JAVADOCS $TEMP_FOLDER/javadocs

echo "Switching to $PUBLISH_BRANCH..."

git checkout $PUBLISH_BRANCH

echo "Replacing it with built javadocs..."

rm -rf *

mv $TEMP_FOLDER/* ./

echo "Committing..."

git add -A .
git commit -m 'Update generated docs.'

echo "Pushing..."

git push origin $PUBLISH_BRANCH

git checkout $INITIAL_COMMIT
