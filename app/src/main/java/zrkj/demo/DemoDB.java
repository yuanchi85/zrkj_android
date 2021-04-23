//package zrkj.demo;
//
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.finddreams.baselib.R;
//import com.finddreams.baselib.base.BaseActivity;
//import com.lidroid.xutils.view.annotation.ContentView;
//import com.lidroid.xutils.view.annotation.event.OnTouch;
//
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmResults;
//import io.realm.Sort;
//
//@ContentView(R.layout.demo_db)
//public class DemoDB extends BaseActivity {
//
//	@Override
//	protected void onScan(String barcodeStr) {
//
//	}
//
//	@Override
//	protected void initView() {
//
//	}
//
//	@Override
//	protected void initData() {
//		// 使用默认配置初始化数据库
//		Realm.setDefaultConfiguration(new RealmConfiguration.Builder(context)
//				.build());
//	}
//
//	@OnTouch({ R.id.btinsert, R.id.btupdate, R.id.btdel, R.id.btselect })
//	private <T> void menu1(View v, MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			v.getBackground().setAlpha(20);
//		} else if (event.getAction() == MotionEvent.ACTION_UP) {
//			v.getBackground().setAlpha(255);// 设置的透明度
//			if (v.getId() == R.id.btinsert) {
//				// 类型一 ：新建一个对象，并进行存储
//				Realm realm1 = Realm.getDefaultInstance();
//
//				realm1.beginTransaction();
//				DbObject dbObject1 = realm1.createObject(DbObject.class); // Create
//																			// a
//																			// new
//																			// object
//				dbObject1.setName("John");
//				dbObject1.setAge(10);
//				realm1.commitTransaction();
//				// 类型二：复制一个对象到Realm数据库
//
//				Realm realm2 = Realm.getDefaultInstance();
//
//				DbObject dbObject2 = new DbObject();
//				dbObject2.setName("John");
//				dbObject2.setAge(10);
//				realm2.beginTransaction();
//				realm2.copyToRealm(dbObject2);
//				realm2.commitTransaction();
//
//				// 实现方法二：使用事务块
//				Realm Realm3 = Realm.getDefaultInstance();
//
//				final DbObject dbObject3 = new DbObject();
//				dbObject3.setName("John");
//				dbObject3.setAge(10);
//
//				Realm3.executeTransaction(new Realm.Transaction() {
//					@Override
//					public void execute(Realm realm) {
//
//						realm.copyToRealm(dbObject3);
//
//					}
//				});
//			} else if (v.getId() == R.id.btupdate) {
//				Realm mRealm = Realm.getDefaultInstance();
//
//				DbObject dbObject = mRealm.where(DbObject.class)
//						.equalTo("name", "John").findFirst();
//				mRealm.beginTransaction();
//				dbObject.setName("John");
//				mRealm.commitTransaction();
//			} else if (v.getId() == R.id.btdel) {
//				Realm mRealm = Realm.getDefaultInstance();
//				final RealmResults<DbObject> dbObjects = mRealm.where(
//						DbObject.class).findAll();
//				mRealm.executeTransaction(new Realm.Transaction() {
//					@Override
//					public void execute(Realm realm) {
//						DbObject dbObject = dbObjects.get(5);
//						// 删除第几个数据
//						dbObjects.remove(dbObjects.get(5));
//						dbObjects.remove(dbObjects.size());
//					}
//				});
//			} else if (v.getId() == R.id.btselect) {
//				Realm mRealm = Realm.getDefaultInstance();
//				DbObject dbObject = mRealm.where(DbObject.class)
//						.equalTo("name", "John").findFirst();
//				RealmResults<DbObject> dbObjects = mRealm.where(DbObject.class)
//						.findAll();
//				dbObjects.sort("id");
//				// 降序排列
//				dbObjects.sort("id", Sort.DESCENDING);
//				// between(), greaterThan(), lessThan(), greaterThanOrEqualTo()
//				// & lessThanOrEqualTo()
//				//
//				// equalTo() & notEqualTo()
//				//
//				// contains(), beginsWith() & endsWith()
//				//
//				// isNull() & isNotNull()
//				//
//				// isEmpty() & isNotEmpty()
//
//				//sum，min，max，average只支持整型数据字段
//
//			}
//		}
//	}
//
////	（1）异步增：
////    private void addCat(final Cat cat) {
////      RealmAsyncTask  addTask=  mRealm.executeTransactionAsync(new Realm.Transaction() {
////            @Override
////            public void execute(Realm realm) {
////                realm.copyToRealm(cat);
////            }
////        }, new Realm.Transaction.OnSuccess() {
////            @Override
////            public void onSuccess() {
////                ToastUtil.showShortToast(mContext,"收藏成功");
////            }
////        }, new Realm.Transaction.OnError() {
////            @Override
////            public void onError(Throwable error) {
////                ToastUtil.showShortToast(mContext,"收藏失败");
////            }
////        });
////
////    }
////最后在销毁Activity或Fragment时，要取消掉异步任务
////
////@Override
////    protected void onDestroy() {
////        super.onDestroy();
////       if (addTask!=null&&!addTask.isCancelled()){
////            addTask.cancel();
////        }
////    }
////（2）异步删
////    private void deleteCat(final String id, final ImageView imageView){
////      RealmAsyncTask  deleteTask=   mRealm.executeTransactionAsync(new Realm.Transaction() {
////            @Override
////            public void execute(Realm realm) {
////                Cat cat=realm.where(Cat.class).equalTo("id",id).findFirst();
////                cat.deleteFromRealm();
////
////            }
////        }, new Realm.Transaction.OnSuccess() {
////            @Override
////            public void onSuccess() {
////                ToastUtil.showShortToast(mContext,"取消收藏成功");
////            }
////        }, new Realm.Transaction.OnError() {
////            @Override
////            public void onError(Throwable error) {
////                ToastUtil.showShortToast(mContext,"取消收藏失败");
////            }
////        });
////
////    }
////最后在销毁Activity或Fragment时，要取消掉异步任务
////
////@Override
////    protected void onDestroy() {
////        super.onDestroy();
////       if (deleteTask!=null&&!addTask.isCancelled()){
////            deleteTask.cancel();
////        }
////    }
////（3）异步改
////RealmAsyncTask  updateTask=   mRealm.executeTransactionAsync(new Realm.Transaction() {
////            @Override
////            public void execute(Realm realm) {
////                Cat cat=realm.where(Cat.class).equalTo("id",mId).findFirst();
////                cat.setName(name);
////            }
////        }, new Realm.Transaction.OnSuccess() {
////            @Override
////            public void onSuccess() {
////                ToastUtil.showShortToast(UpdateCatActivity.this,"更新成功");
////
////            }
////        }, new Realm.Transaction.OnError() {
////            @Override
////            public void onError(Throwable error) {
////                ToastUtil.showShortToast(UpdateCatActivity.this,"失败成功");
////            }
////        });
////最后在销毁Activity或Fragment时，要取消掉异步任务
////
////@Override
////    protected void onDestroy() {
////        super.onDestroy();
////       if (updateTask!=null&&!addTask.isCancelled()){
////            updateTask.cancel();
////        }
////    }
////（4）异步查
////
////     RealmResults<Cat>   cats=mRealm.where(Cat.class).findAllAsync();
////        cats.addChangeListener(new RealmChangeListener<RealmResults<Cat>>() {
////            @Override
////            public void onChange(RealmResults<Cat> element) {
////               element= element.sort("id");
////                List<Cat> datas=mRealm.copyFromRealm(element);
////
////            }
////        });
////最后在销毁Activity或Fragment时，要取消掉异步任务
////
//// @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        cats.removeChangeListeners();
////
////    }
//}