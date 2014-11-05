# clojure.osgi.tools

A Clojure library providing utilities for working with miscellaneous OSGi tools (BND, PDE, etc.).

## Usage

### Generate BND lib property files out of Eclipse features

From REPL:
```
=> (clojure.osgi.tools.bnd/create-libs-from-pde-features "/tmp/input-dir" "/tmp/ouput-dir")
```

From command line:
```
lein run -m clojure.osgi.tools.bnd/create-libs-from-pde-features "/tmp/input-dir" "/tmp/output-dir"
```

## License

Copyright © 2014 Suprematic Solutions

Distributed under the Eclipse Public License 1.0. See <https://www.eclipse.org/legal/epl-v10.html>.

## Feedback

Contact us at <info@suprematic.net>.
