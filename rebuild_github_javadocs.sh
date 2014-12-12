#!/bin/bash

set -e

INITIAL_COMMIT=$(echo $( git symbolic-ref HEAD 2> /dev/null || git rev-parse --short HEAD 2> /dev/null ) | sed "s#refs/heads/##")
PUBLISH_BRANCH=gh-pages
JAVADOCS=recyclerview-multiselect/build/docs/javadoc
TEMP_FOLDER=/tmp/$(basename $JAVADOCS).$$.tmp

rm -rf $TEMP_FOLDER

echo 'Building javadocs...'

./gradlew generateReleaseJavadoc

mv $JAVADOCS $TEMP_FOLDER 

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
