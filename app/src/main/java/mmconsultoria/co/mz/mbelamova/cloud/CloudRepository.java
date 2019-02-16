package mmconsultoria.co.mz.mbelamova.cloud;

import android.net.Uri;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mmconsultoria.co.mz.mbelamova.model.BaseActivity;
import timber.log.Timber;

import static java.lang.String.valueOf;

public class CloudRepository<T extends ImageHolder> {
    private DatabaseReference dataRef;
    private MutableLiveData<T> singleLiveData;
    private Class<T> classType;

    private List<T> list;
    private LiveData<List<T>> listLiveData;
    private DatabaseValue root;


    public CloudRepository(Class<T> classType, DatabaseValue root) {
        this.classType = classType;
        if (root == null) {
            throw new IllegalArgumentException("DatabaseValue cannot be null");
        }

        this.root = root;

        dataRef = FirebaseDatabase.getInstance().getReference();
        listLiveData = new MutableLiveData<>();
        singleLiveData = new MutableLiveData<>();


        dataRef = dataRef.child(root.name());
    }

    public Single<TaskError> upload(@NonNull T data, BaseActivity activity) {
        Uri uri = Uri.parse(data.getPhotoUri());
        Timber.d(valueOf(uri));

        if (data == null) {
            throw new NullPointerException("data field cannot be null!");
        }

        return Single.<TaskError>create(emitter -> {
            DatabaseReference dataRef = this.dataRef.push();

            if (uri == null || uri.toString().isEmpty()) {
                uploadData(dataRef, data)
                        .observeOn(Schedulers.io())
                        .subscribe(emitter::onSuccess, emitter::onError);
            } else uploadImage(dataRef.getKey(), uri, activity)
                    .observeOn(Schedulers.io()).subscribe(dataUri -> {
                                data.setPhotoUri(dataUri.toString());
                                uploadData(dataRef, data)
                                        .observeOn(Schedulers.io())
                                        .subscribe(emitter::onSuccess, emitter::onError);
                            }
                    );
        });

    }

    private Single<Uri> uploadImage(String fileName, @NonNull Uri uri, BaseActivity activity) {
        Timber.d(root.name());
        Timber.d(uri.toString());
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference(root.name())
                .child(fileName +"."+
                        activity.getFileExtension(uri));


        return Single.<Uri>create(emitter -> {

            reference.putFile(uri).continueWithTask(
                    task -> {
                        if (!task.isSuccessful()) {
                            Timber.d(valueOf(task));
                            emitter.onError(task.getException());
                        }

                        // Continue with the task to get the download URL

                        return reference.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Timber.d(valueOf(downloadUri));
                    emitter.onSuccess(downloadUri);
                } else {
                    emitter.onSuccess(Uri.EMPTY);
                }
            }).addOnFailureListener(emitter::onError);
        });

    }

    private Single<TaskError> uploadData(DatabaseReference dataRef, @NonNull T data) {

        return Single.<TaskError>create(emitter -> {
            dataRef.setValue(data).addOnSuccessListener(success -> {
                emitter.onSuccess(TaskError.None);
            }).addOnFailureListener(failure -> {
                Timber.d(failure.getCause());
                emitter.onError(failure);
            });
        });

    }

    public void removeChildFromCloud(String id, final OnChildValueListener<T> onRemoveValue) {
        dataRef.child(id).removeValue(((databaseError, databaseReference) -> {
            if (onRemoveValue == null) {
                return;
            }

            if (hasError(onRemoveValue, databaseError, DatabaseMovement.Removal)) {
                return;
            }

            onRemoveValue.onMovement(null, TaskError.None, DatabaseMovement.Removal);
        }));

    }

    private boolean hasError(final CloudListener<T> listener, DatabaseError databaseError, DatabaseMovement movement) {

        if (databaseError.getCode() == DatabaseError.NETWORK_ERROR) {
            listener.onMovement(null, TaskError.Network, movement);
            return true;
        }

        if (databaseError.getCode() == DatabaseError.DISCONNECTED) {
            listener.onMovement(null, TaskError.Network, movement);
            return true;
        }

        if (databaseError.getCode() == DatabaseError.UNKNOWN_ERROR) {
            listener.onMovement(null, TaskError.Unknown, movement);
            return true;
        }

        if (databaseError.getCode() == DatabaseError.WRITE_CANCELED) {
            listener.onMovement(null, TaskError.Operation_Canceled, movement);
            return true;
        }

        return false;
    }


    public void attachListener(OnChildValueListener<T> listener) {

        if (listener == null) {
            return;
        }


        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final T value = dataSnapshot.getValue(classType);
                listener.onMovement(value, TaskError.None, DatabaseMovement.Addition);

                if (list != null) {
                    list.add(value);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Update);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Removal);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Moved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (hasError(listener, databaseError, DatabaseMovement.Canceled)) return;

                listener.onMovement(null, TaskError.Unknown, DatabaseMovement.Canceled);
            }
        });

    }

    public void attachListener(OnSingleValueListener<T> listener) {

        if (listener == null) {
            return;
        }


        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final T value = dataSnapshot.getValue(classType);
                listener.onMovement(value, TaskError.None, DatabaseMovement.Addition);

                if (list != null) {
                    list.add(value);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Update);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Removal);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onMovement(dataSnapshot.getValue(classType), TaskError.None, DatabaseMovement.Moved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (hasError(listener, databaseError, DatabaseMovement.Canceled)) return;

                listener.onMovement(null, TaskError.Unknown, DatabaseMovement.Canceled);
            }
        });

    }


    public void setPath(String path) {
        dataRef = dataRef.child(path);

    }

    public MutableLiveData<T> getSingleLiveData() {
        return singleLiveData;
    }

    public LiveData<List<T>> getListLiveData() {
        return listLiveData;
    }

    public void setSubPath(CharSequence subPath) {
        dataRef = dataRef.child(subPath.toString());
    }

    public enum DatabaseMovement {
        Addition, Update, Moved, Canceled, Removal
    }

    public interface OnChildValueListener<T extends ImageHolder> extends CloudListener<T> {
    }

    public interface OnSingleValueListener<T extends ImageHolder> extends CloudListener<T> {
    }

    public interface CloudListener<T extends ImageHolder> {
        void onMovement(T data, TaskError error, DatabaseMovement movement);


        interface OnCompleteListener {
            /**
             * @param success true if the action result is successful
             * @param error   Error.None if is successful
             */
            void onComplete(boolean success, TaskError error);
        }

        interface OnStorageCompleteListener {
            /**
             * @param success true if the action result is successful
             * @param error   Error.None if is successful
             */
            void onComplete(boolean success, Uri downloadUri, TaskError error);
        }
    }
}
