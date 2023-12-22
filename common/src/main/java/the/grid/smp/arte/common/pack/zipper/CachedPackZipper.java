package the.grid.smp.arte.common.pack.zipper;

import the.grid.smp.arte.common.Arte;
import the.grid.smp.arte.common.data.FilterList;
import the.grid.smp.arte.common.pack.meta.BuiltPack;
import the.grid.smp.arte.common.pack.meta.namespace.NamespaceLike;
import the.grid.smp.arte.common.util.ThreadPool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class CachedPackZipper extends BasicPackZipper {

    public CachedPackZipper(Arte arte, Path root, Path output) throws IOException {
        super(arte, root, output);
    }

    @Override
    protected Context createContext() {
        return new CachedContext(this.root, this.output, "pack.mcmeta", "pack.png");
    }

    public static class CachedContext extends PackZipper.Context {

        public CachedContext(Path root, Path output, String... defaults) {
            super(root, output, defaults);
        }

        @Override
        public Collection<BuiltPack> zip(FilterList list, boolean scramble, Consumer<BuiltPack> consumer) {
            List<BuiltPack> packs = new ArrayList<>();
            ThreadPool pool = new ThreadPool();

            for (NamespaceLike namespace : this.namespaces) {
                pool.addCatchable(() -> {
                    try {
                        Path generated = this.output.resolve(namespace.name() + ".zip");

                        boolean force = !(list.elements().contains(
                                namespace.name())
                        ) && list.whitelist();

                        BuiltPack pack = new BuiltPack(generated, force);

                        consumer.accept(pack);
                        packs.add(pack);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            pool.start();
            return packs;
        }
    }
}
