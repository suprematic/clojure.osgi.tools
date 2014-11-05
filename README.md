# clojure.osgi.tools

A Clojure library providing utilities for working with miscellaneous OSGi tools (BND, PDE, etc.).

## Usage

### Generate BND lib property files out of Eclipse features
Recursively walks through the given input and generates the BND lib files into the given output directory:
* takes care of feature.xml files and jared features
* output format is a BND cnf/lib directory structure
* the libs are prefixed with "feature_" and preserve original Eclipse feature id and version
* bundles with restricted os/ws/arch/nl capabilities are extracted into a separate BND lib

From REPL:
```
=> (clojure.osgi.tools.bnd/create-libs-from-pde-features "/tmp/eclipse/R-3.8.2/features" "/tmp/bnd-libs-R.3.8.2")
```

From command line:
```
lein run -m clojure.osgi.tools.bnd/create-libs-from-pde-features /tmp/eclipse/R-3.8.2/features /tmp/bnd-libs-R.3.8.2
```

## License

Copyright © 2014 Suprematic Solutions

Distributed under the Eclipse Public License 1.0. See <https://www.eclipse.org/legal/epl-v10.html>.

## Feedback

Contact us at <info@suprematic.net>.
